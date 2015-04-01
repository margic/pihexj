package com.margic.adafruitpwm;

import com.margic.pihex.support.ByteUtils;
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

    private static final Logger log = LoggerFactory.getLogger(AdaPCA9685Device.class);

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
        log.trace("Write register: {} value: {}", address, ByteUtils.byte2Hex(value));
        i2cDevice.write(address, value);
    }

    @Override
    public void writeRegisters(int startAddress, byte[] values) throws IOException {
        log.error("NOT YET IMPLEMENTED - currently focusing on single servo operations");
        i2cDevice.write(startAddress, values, 0, values.length);
    }

    @Override
    public int readRegister(int address) throws IOException {
        int value = i2cDevice.read(address);
        log.trace("Read register: {} value {}", address, Integer.toHexString(value));
        return value;
    }

    @Override
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) throws IOException {
        log.error("NOT YET IMPLEMENTED");
        return i2cDevice.read(startAddress, buffer, 0, buffer.length);
    }
}
