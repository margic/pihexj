package com.margic.adafruitpwm;

import com.pi4j.io.i2c.I2CDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/18/15.
 * <p>
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
    public void writeRegister(int address, byte value) throws IOException {
        i2cDevice.write(address, value);
    }

    @Override
    public void writeRegisters(int startAddress, byte[] values) throws IOException {
        LOGGER.error("THIS SHOULD HAVE SET THE AUTO INCREMENT BEFORE THIS CALL RESET AFTER");
        i2cDevice.write(startAddress, values, 0, values.length);
    }

    @Override
    public int readRegister(int address) throws IOException {
        return i2cDevice.read(address);
    }

    @Override
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) throws IOException {
        LOGGER.error("THIS SHOULD HAVE SET THE AUTO INCREMENT BEFORE THIS CALL RESET AFTER");
        return i2cDevice.read(startAddress, buffer, 0, buffer.length);
    }
}
