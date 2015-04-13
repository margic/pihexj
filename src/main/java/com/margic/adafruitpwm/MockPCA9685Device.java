package com.margic.adafruitpwm;

import com.margic.pihex.support.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;


/**
 * Mock device for use in tests
 * The actual device requires and i2c device that requires
 * a native class that is not present during development
 * The mock device allows creation of servo driver and allows testing
 * of setting of registers and recording of byte stream sent to the device.
 * Created by paulcrofts on 3/18/15.
 */
public class MockPCA9685Device implements PCA9685Device {

    private static Logger log = LoggerFactory.getLogger(MockPCA9685Device.class);

    private byte[] registers;
    private ByteArrayOutputStream byteStream;

    private String name;

    public MockPCA9685Device(String name) {
        this.name = name;

        // device has 256 registers
        this.registers = new byte[256];

        // create a byte stream store bytes written to device
        this.byteStream = new ByteArrayOutputStream(1024);

        // set the defaults value first registers
        this.registers[MODE1] = (byte)0x11; // set the default value for mode1
        this.registers[MODE2] = (byte)0x04;
        this.registers[SUBADR1] = (byte)0xE2;
        this.registers[SUBADR2] = (byte)0xE4;
        this.registers[SUBADR3] = (byte)0xE8;
        this.registers[ALLCALLADR] = (byte)0xE0;

        dumpRegisters();
     }

    @Override
    public String getDeviceName() {
        return name;
    }


    @Override
    public void writeRegister(int address, byte value) {
        log.trace("Write register: {} value: {}", address, ByteUtils.byte2Hex(value));
        if (address >= ALL_LED_ON_L && address <= ALL_LED_OFF_H) {
            log.debug("Set all write");

            for (int i = (address - 244); i < 70; i += 4) {
                registers[i] = value;
            }
        } else {
            registers[address] = value;
        }
        byteStream.write(address);
        byteStream.write(value);
    }

    @Override
    public void writeRegisters(int address, byte[] values) {
        for (byte value : values) {
            writeRegister(address, value);
            address++;
        }
    }

    @Override
    public int readRegister(int address) {
        int value = (int)registers[address] & 0xFF;
        log.trace("Read register: {} value {}", address, Integer.toHexString(value));
        return value;
    }

    @Override
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) {
        int count;
        for (count = 0; count < size; count++) {
            buffer[offset + count] = registers[startAddress + count];
        }
        return count;
    }

    public void dumpRegisters() {
        log.info("Registers:" + System.getProperty("line.separator") + ByteUtils.dumpHex(registers));
    }

    public void dumpByteStream() {
        log.info("ByteStream:" + System.getProperty("line.separator") + ByteUtils.dumpHex(byteStream.toByteArray()));
    }
}
