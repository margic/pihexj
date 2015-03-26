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

    private static Logger LOGGER = LoggerFactory.getLogger(MockPCA9685Device.class);

    private static String NAME = "Mock PCA9685 device";

    private byte[] registers;
    private ByteArrayOutputStream byteStream;

    public MockPCA9685Device() {
        // device has 256 registers
        this.registers = new byte[256];

        // create a byte stream store bytes written to device
        this.byteStream = new ByteArrayOutputStream(1024);

        //this.registers[MODE1] = (byte)0x11; // set the default value for mode1
        //this.registers[MODE2] = (byte)0x04;
        dumpRegisters();
     }

    @Override
    public String getDeviceName() {
        return NAME;
    }


    @Override
    public void writeRegister(int address, byte value) {
        LOGGER.trace("Write register: {} value: {}", address, ByteUtils.byte2Hex(value));
        if(address >= ALL_LED_ON_L && address <= ALL_LED_OFF_H){
            LOGGER.debug("Set all write");

            for(int i = (address - 244); i < 70; i+=4){
                registers[i] = value;
            }
        }else {
            registers[address] = value;
        }
        byteStream.write(address);
        byteStream.write(value);



    }

    private void writeAll(int address, byte value){

    }

    @Override
    public void writeRegisters(int startAddress, byte[] values) {
        int address = startAddress;
        for (byte value : values) {
            writeRegister(address, value);
            address++;
        }
    }

    @Override
    public int readRegister(int address) {
        int value = (int)registers[address] & 0xFF;
        LOGGER.trace("Read register: {} value {}", address, Integer.toHexString(value));
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
        LOGGER.info("Registers:" + System.getProperty("line.separator") + ByteUtils.dumpHex(registers));
    }

    public void dumpByteStream() {
        LOGGER.info("ByteStream:" + System.getProperty("line.separator") + ByteUtils.dumpHex(byteStream.toByteArray()));
    }
}
