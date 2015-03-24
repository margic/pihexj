package com.margic.adafruitpwm;

import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.support.ByteUtils;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by paulcrofts on 3/15/15.
 * Servo driver implementation for Adafruit-PCA9685 pwm board
 * See datasheet http://www.adafruit.com/datasheets/PCA9685.pdf
 */
public class AdafruitServoDriver implements ServoDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdafruitServoDriver.class);

    private static final String DRIVER_NAME = "Adafruit-PCA9685";

    private static final int LED_ON_LOW = 0;
    private static final int LED_ON_HIGH = 1;
    private static final int LED_OFF_LOW = 2;
    private static final int LED_OFF_HIGH = 3;


    public static final double CLOCK_FREQUENCY = 25 * 1000000;
    public static final double RESOLUTION = 4096;

    private PCA9685Device device;

    /*
      * cache is use when writing multiple servos to cache any missing
      * servos from the supplied list. The PCA9685 allows writing sequential
      * registers. This avoids a lot of i2c overhead. To take advantage of
      * sequential writes the cache can cache state of previous writes to registers
     */
    private Servo[] servoCache;

    @Inject
    public AdafruitServoDriver(PCA9685Device device) {
        LOGGER.debug("Creating new servo driver {} for device {}", getDriverName(), device.getDeviceName());
        this.device = device;
        this.servoCache = new Servo[16]; // will need to revisit when using more than one board
    }

    @Override
    public String getDriverName() {
        return DRIVER_NAME;
    }

    /**
     * Init the device with the default settings in case existing settings are still in memory
     * @throws IOException
     */
    @Override
    public void init() throws IOException{
        LOGGER.info("Initializing the device to preferred start up state"); // essentially the power on default

        LOGGER.debug("Setting all registers off using ALL registers");
        device.writeRegister(PCA9685Device.ALL_LED_ON_L, (byte) 0x00);
        device.writeRegister(PCA9685Device.ALL_LED_ON_H, (byte) 0x00);
        device.writeRegister(PCA9685Device.ALL_LED_OFF_L, (byte) 0x00);
        device.writeRegister(PCA9685Device.ALL_LED_OFF_H, (byte) 0x00);

        LOGGER.debug("Set mode 2 with only OUTDRV bit high: {}", PCA9685Device.OUTDRV);
        device.writeRegister(PCA9685Device.MODE2, (byte)PCA9685Device.OUTDRV);
        LOGGER.debug("Set mode 1 with only ALLCALL bit high: {}", PCA9685Device.ALLCALL); // this sets oscillator on
        device.writeRegister(PCA9685Device.MODE1, (byte)PCA9685Device.ALLCALL);
        // with for oscillator to settle takes 500 micro seconds, 50 ms is way more than needed
        sleep(50);
    }

    /**
     * The hardware forces a minimum value that can be loaded into the PRE_SCALE register
     * at ‘3’. The PRE_SCALE register defines the frequency at which the outputs modulate. The
     * prescale value is determined with the formula shown in Equation 1:
     * (1) prescaleVal = round(oscClock/(4096 * updateRate)) - 1
     * where the update rate is the output modulation frequency required. For example, for an
     * output frequency of 200 Hz with an oscillator clock frequency of 25 MHz:
     * (2) prescaleVal = round(25000000 / (4096 * 200)) - 1 = 30
     * The PRE_SCALE register can only be set when the SLEEP bit of MODE1 register is set to
     * logic 1.
     *
     * @param frequency the frequency in hertz
     */
    @Override
    public void setPulseFrequency(int frequency) throws IOException {
        LOGGER.info("Setting pwm frequency to {} hz", frequency);

        int prescale = getPreScale(frequency);

        LOGGER.debug("Reading value of Mode 1 register");
        int oldMode1 = device.readRegister(PCA9685Device.MODE1);
        LOGGER.debug("Mode 1 register: {}", Integer.toHexString(oldMode1));

        int newMode1 = (oldMode1 & 0x7F) | PCA9685Device.MODE1_SLEEP;
        LOGGER.debug("Setting sleep bit on Mode 1 register: {}", Integer.toHexString(newMode1));
        device.writeRegister(PCA9685Device.MODE1, (byte)newMode1);

        LOGGER.debug("Writing prescale register with: {}", Integer.toHexString(prescale));
        device.writeRegister(PCA9685Device.PRESCALE, (byte)prescale);

        newMode1 = oldMode1 & ~PCA9685Device.MODE1_SLEEP;
        LOGGER.debug("Writing the old value back to mode1 register with sleep off to start osc again: {}", Integer.toHexString(newMode1));
        device.writeRegister(PCA9685Device.MODE1, (byte)(newMode1));
        // wait for oscillator to restart
        sleep(50);
        newMode1 = oldMode1 | PCA9685Device.MODE1_RESTART;
        LOGGER.debug("Setting restart bit: {}", Integer.toHexString(newMode1));
        device.writeRegister(PCA9685Device.MODE1, (byte)newMode1);
    }



    public int getPreScale(int frequency) throws IOException{
        LOGGER.debug("Get prescale value for frequency {}", frequency);

        double correctedFrequency = frequency * 0.9;  // Correct for overshoot in the frequency setting (see issue #11).
        double prescaleval = CLOCK_FREQUENCY;
        prescaleval /= RESOLUTION;
        prescaleval /= correctedFrequency;
        prescaleval -= 1.0;
        LOGGER.debug("Estimated pre-scale {}", prescaleval);
        prescaleval = Math.round(prescaleval + 0.5);

        if(prescaleval > 254){
            throw new IOException("Specified frequency " + frequency + " results in prescale value " + prescaleval + " that exceed limit 254");
        }
        int prescale = (int)prescaleval;
        LOGGER.debug("Final pre-scale {}", prescale);
        return prescale;
    }

    @Override
    public void updateServos(List<Servo> servos) throws IOException {
        //todo update implementation with version that using sequential writes to PCA9685
        for(Servo servo: servos){
            updateServo(servo);
        }
    }

    @Override
    public void updateServo(Servo servo) throws IOException {
        // update cache first
        cacheServo(servo);
        int servoChannel = servo.getChannel();
        device.writeRegister(getRegisterForChannel(servoChannel, Register.ON_LOW), (byte) 0x00);
        device.writeRegister(getRegisterForChannel(servoChannel, Register.ON_HIGH), (byte) 0x00);
        byte[] offBytes = ByteUtils.get2ByteInt(servo.getPulseLength(servo.getAngle()));
        device.writeRegister(getRegisterForChannel(servoChannel, Register.OFF_LOW), offBytes[ByteUtils.HIGH_BYTE]);
        device.writeRegister(getRegisterForChannel(servoChannel, Register.OFF_HIGH), offBytes[ByteUtils.LOW_BYTE]);
    }

    private void cacheServo(Servo servo){
        int servoChannel = servo.getChannel();
        servoCache[servoChannel] = servo;
    }

    /**
     * Returns one of the 4 channel0 registers addresses based on servo channel
     * @param channel
     * @param register

     * @return
     */
    private int getRegisterForChannel(int channel, Register register){
        return channel * register.value + PCA9685Device.LED0_ON_HIGH; // LED0_ON_HIGH is first of sequence of registers
    }



    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep interrupted.", e);
        }
    }

    private enum Register{
        ON_LOW(0),
        ON_HIGH(1),
        OFF_LOW(2),
        OFF_HIGH(3);

        private final int value;
        Register(int value){
            this.value = value;
        }

        int value(){
            return value;
        }

    }
}
