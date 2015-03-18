package com.margic.adafruitpwm;

import com.margic.servo4j.ServoDriver;
import com.pi4j.io.i2c.I2CDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by paulcrofts on 3/15/15.
 * Servo driver implementation for Adafruit-PCA0685 pwm board
 * See datasheet http://www.adafruit.com/datasheets/PCA9685.pdf
 */
public class AdafruitServoDriver implements ServoDriver {

    private static final Logger log = LoggerFactory.getLogger(AdafruitServoDriver.class);

    private static final String DRIVER_NAME = "Adafruit-PCA9685";

    public static final double CLOCK_FREQUENCY = 25 * 1000000;
    public static final double RESOLUTION = 4096;
    public static final int DEFAULT_PWM_FREQUENCY = 50;

    public static final int MODE1_REGISTER = 0x00;
    public static final int PRE_SCALE_REGISTER = 0xFE;

    // in my use case a hex robot I will need more than one device. or possibly two driver instances. not sure yet
    // but I'll fix that whenever I get to that point.
    private I2CDevice i2CDevice;

    @Inject
    public AdafruitServoDriver(I2CDevice i2CDevice){
        log.debug("Creating new servo driver {}", getDriverName());
        this.i2CDevice = i2CDevice;
    }

    @Override
    public String getDriverName() {
        return DRIVER_NAME;
    }

    /**
     * The hardware forces a minimum value that can be loaded into the PRE_SCALE register
     at ‘3’. The PRE_SCALE register defines the frequency at which the outputs modulate. The
     prescale value is determined with the formula shown in Equation 1:
     (1) prescaleVal = round(oscClock/(4096 * updateRate)) - 1
     where the update rate is the output modulation frequency required. For example, for an
     output frequency of 200 Hz with an oscillator clock frequency of 25 MHz:
     (2) prescaleVal = round(25000000 / (4096 * 200)) - 1 = 30
     The PRE_SCALE register can only be set when the SLEEP bit of MODE1 register is set to
     logic 1.
     * @param frequency the frequency in hertz
     */
    @Override
    public void setPWMFrequency(int frequency) {
        log.debug("Setting pwm frequency to {} hz", frequency);

        // Writes to PRE_SCALE register are blocked when SLEEP bit is logic 0 (MODE 1)
        log.debug("Read MODE 1 Register");
        //int origMode1 =



//        double prescaleval = 25000000.0;
//        prescaleval /= 4096.0;
//        prescaleval /= frequency; // Correct for overshoot in the frequency setting (see issue #11).
//        prescaleval -= 1.0;
//        log.debug("Estimated pre-scale {}", prescaleval);
//        double prescale = Math.floor(prescaleval + 0.5);
//        log.debug("Final pre-scale {}", prescale);
//        int oldmode = i2cDevice.read(MODE1);
//        int newmode = (oldmode & 0x7F) | 0x10;
//        write(MODE1, (byte) newmode);
//        write(PRESCALE, (byte) (Math.floor(prescale)));
//        write(MODE1, (byte) oldmode);
//        sleep(50);
//        write(MODE1, (byte) (oldmode | 0x80));
    }

    public byte getPreScale(int frequency){
        log.debug("Get prescale value for frequency {}", frequency);

        double prescaleval = CLOCK_FREQUENCY;
        prescaleval /= RESOLUTION;
        prescaleval /= frequency;
        prescaleval -= 1.0;
        log.debug("Estimated pre-scale {}", prescaleval);
        byte prescale = (byte)Math.floor(prescaleval + 0.5);
        log.debug("Final pre-scale {}", prescale);

        return prescale;
    }

}
