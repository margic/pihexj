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
    public void writeRegister(int address, byte value) {
    }

    @Override
    public void writeRegisters(int startAddress, byte[] values) {

    }

    @Override
    public byte readRegister(int address) {
        return 0;
    }

    @Override
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) {
        return 0;
    }
}
