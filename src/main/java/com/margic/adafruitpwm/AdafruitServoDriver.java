package com.margic.adafruitpwm;

import com.margic.servo4j.ServoDriver;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by paulcrofts on 3/15/15.
 * Servo driver implementation for Adafruit-PCA0685 pwm board
 * See datasheet http://www.adafruit.com/datasheets/PCA9685.pdf
 */
public class AdafruitServoDriver implements ServoDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdafruitServoDriver.class);

    private static final String DRIVER_NAME = "Adafruit-PCA9685";

    public static final double CLOCK_FREQUENCY = 25 * 1000000;
    public static final double RESOLUTION = 4096;

    private PCA9685Device device;
    private Configuration config;

    @Inject
    public AdafruitServoDriver(Configuration config, PCA9685Device device) {
        LOGGER.debug("Creating new servo driver {} for device {}", getDriverName(), device.getDeviceName());
        this.device = device;
        this.config = config;
    }

    @Override
    public String getDriverName() {
        return DRIVER_NAME;
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
    public void setPWMFrequency(int frequency) throws IOException {
        LOGGER.debug("Setting pwm frequency to {} hz", frequency);

        // Writes to PRE_SCALE register are blocked when SLEEP bit is logic 0 (MODE 1)
        LOGGER.debug("Read MODE 1 Register");
        //int origMode1 =

        int prescale = getPreScale(frequency);


        LOGGER.debug("Reading value of Mode 1 register");
        int oldMode1 = device.readRegister(PCA9685Device.MODE1);
        LOGGER.debug("Mode 1 register {}", Integer.toHexString(oldMode1));

        int newMode1 = (oldMode1 & 0x7F) | PCA9685Device.MODE1_SLEEP;
        LOGGER.debug("Setting sleep bit on Mode 1 register {}", Integer.toHexString(newMode1));
        device.writeRegister(PCA9685Device.MODE1, (byte)newMode1);

        LOGGER.debug("Writing prescale register with {}", prescale);
        device.writeRegister(PCA9685Device.PRESCALE, (byte)prescale);

        LOGGER.debug("Writing the old value back to mode1 register to start osc again");
        device.writeRegister(PCA9685Device.MODE1, (byte)oldMode1);
        // wait for oscillator to restart
        sleep(50);
        device.writeRegister(PCA9685Device.MODE1, (byte)(oldMode1 | PCA9685Device.MODE1_RESTART));
    }

    public int getPreScale(int frequency) throws IOException{
        LOGGER.debug("Get prescale value for frequency {}", frequency);

        double prescaleval = CLOCK_FREQUENCY;
        prescaleval /= RESOLUTION;
        prescaleval /= frequency;
        prescaleval -= 1.0;
        LOGGER.debug("Estimated pre-scale {}", prescaleval);
        prescaleval = Math.floor(prescaleval + 0.5);

        if(prescaleval > 254){
            throw new IOException("Specified frequency " + frequency + " results in prescale value " + prescaleval + " that exceed limit 254");
        }
        int prescale = (int)prescaleval;
        LOGGER.debug("Final pre-scale {}", prescale);
        return prescale;
    }

    @Override
    public void setPulse(int pulseLength) {

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("sleep interrupted.", e);
        }
    }
}
