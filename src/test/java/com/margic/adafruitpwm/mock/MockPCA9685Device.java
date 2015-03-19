package com.margic.adafruitpwm.mock;

import com.margic.adafruitpwm.PCA9685Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.HexDumpEncoder;

import java.io.ByteArrayOutputStream;


/**
 * Mock device for use in tests
 * The actual device requires and i2c device that requires
 * a native class that is not present during development
 * The mock device allows creation of servo driver and allows testing
 * of setting of registers and recording of byte stream sent to the device.
 * Created by paulcrofts on 3/18/15.
 */
public class MockPCA9685Device implements PCA9685Device{

    private static Logger LOGGER = LoggerFactory.getLogger(MockPCA9685Device.class);

    private static String NAME = "Mock PCA9685 device";

    private byte[] registers;
    private ByteArrayOutputStream byteStream;

    private HexDumpEncoder dumpEncoder;


    public MockPCA9685Device(){
        // device has 256 registers
        this.registers = new byte[256];
        // create a byte stream store bytes written to device
        this.byteStream = new ByteArrayOutputStream(1024);
        dumpEncoder = new HexDumpEncoder();
    }

    @Override
    public String getDeviceName() {
        return NAME;
    }

    @Override
    public void init() {

    }

    @Override
    public void writeRegister(int address, byte value) {
        registers[address] = value;
        byteStream.write(value);
    }

    @Override
    public void writeRegisters(int startAddress, byte[] values) {
        int address = startAddress;
        for(byte value: values){
            writeRegister(address, value);
            address++;
        }
    }

    @Override
    public byte readRegister(int address) {
        return registers[address];
    }

    @Override
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) {
        return 0;
    }

    public void dumpRegisters(){
        LOGGER.info("Registers:" + System.getProperty("line.separator") + dumpEncoder.encode(registers));
    }

    public void dumpByteStream(){
        LOGGER.info("ByteStream:" + System.getProperty("line.separator") + dumpEncoder.encode(byteStream.toByteArray()));
    }
}