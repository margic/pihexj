package com.margic.adafruitpwm;

import com.pi4j.io.i2c.I2CDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/18/15.
 * <p/>
 * Device object providing PCA9685 functions
 */
public class AdaPCA9685Device implements PCA9685Device {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdaPCA9685Device.class);

    private static final String DEVICE_NAME = "Adafruit PCA9685 Device";

    private I2CDevice i2cDevice;

    public AdaPCA9685Device(I2CDevice i2cDevice) {
        this.i2cDevice = i2cDevice;
    }

    @Override
    public String getDeviceName() {
        return DEVICE_NAME;
    }

    @Override
    public void init() {

    }

    @Override
    public void setAllLEDPulse(int[] servoPulseArray) {

    }

    @Override
    public void setLEDPulse(int ledChannel, int pulseLength) {

    }
}
