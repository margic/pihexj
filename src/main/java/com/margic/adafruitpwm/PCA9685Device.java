package com.margic.adafruitpwm;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/18/15.
 * The PCA9685 device is a set of LED PWM drivers.
 * The register definitions provide the registers for setting LED on and off times
 * that is then cycled by a prescaled cycle time.
 * <p>
 * This interface defines the methods and constants for interfacing with a device.
 * The LED registers set the on time and off time of a pulse. The resolution is 4096
 * An on time of 0 and and off time of 409 represents a duty cycle of ~10%
 * The duration of that depends on the Oscillator frequency.
 * //TODO verify
 * <p>
 * Remark: Auto Increment past register 69 will point to MODE1 register (register 0).
 * Auto Increment also works from register 250 to register 254, then rolls over to register 0.
 */
public interface PCA9685Device {

    // PCA9685 Register Definitions
    public static final int MODE1 = 0x00; // mode register 1
    public static final int MODE2 = 0x01; // mode register 2
    public static final int SUBADR1 = 0x02; // i2c bus subaddress 1
    public static final int SUBADR2 = 0x03; // i2c bus subaddress 2
    public static final int SUBADR13 = 0x04; // i2c bus subaddress 3
    public static final int ALLCALLADR = 0x05; // LED All call i2c bus address

    // begin LED registers.
    // channel 0
    public static final int LED0_ON_LOW = 0x06; // LED0 on time low byte
    public static final int LED0_ON_HIGH = 0x07; // LED0 on time high byte
    public static final int LED0_OFF_LOW = 0x08; // LED0 off time low byte
    public static final int LED0_OFF_HIGH = 0x09; // LED0 off time high byte

    // channel 1
    public static final int LED1_ON_LOW = 0x0A; // LED1 on time low byte
    public static final int LED1_ON_HIGH = 0x0B; // LED1 on time high byte
    public static final int LED1_OFF_LOW = 0x0C; // LED1 off time low byte
    public static final int LED1_OFF_HIGH = 0x0D; // LED1 off time high byte     

    // channel 2
    public static final int LED2_ON_LOW = 0x0E; // LED2 on time low byte
    public static final int LED2_ON_HIGH = 0x0F; // LED2 on time high byte
    public static final int LED2_OFF_LOW = 0x10; // LED2 off time low byte
    public static final int LED2_OFF_HIGH = 0x11; // LED2 off time high byte

    // channel 3
    public static final int LED3_ON_LOW = 0x12; // LED3 on time low byte
    public static final int LED3_ON_HIGH = 0x13; // LED3 on time high byte
    public static final int LED3_OFF_LOW = 0x14; // LED3 off time low byte
    public static final int LED3_OFF_HIGH = 0x15; // LED3 off time high byte

    // channel 4
    public static final int LED4_ON_LOW = 0x16; // LED4 on time low byte
    public static final int LED4_ON_HIGH = 0x17; // LED4 on time high byte
    public static final int LED4_OFF_LOW = 0x18; // LED4 off time low byte
    public static final int LED4_OFF_HIGH = 0x19; // LED4 off time high byte

    // channel 5
    public static final int LED5_ON_LOW = 0x1A; // LED5 on time low byte
    public static final int LED5_ON_HIGH = 0x1B; // LED5 on time high byte
    public static final int LED5_OFF_LOW = 0x1C; // LED5 off time low byte
    public static final int LED5_OFF_HIGH = 0x1D; // LED5 off time high byte

    // channel 6
    public static final int LED6_ON_LOW = 0x1E; // LED6 on time low byte
    public static final int LED6_ON_HIGH = 0x1F; // LED6 on time high byte
    public static final int LED6_OFF_LOW = 0x20; // LED6 off time low byte
    public static final int LED6_OFF_HIGH = 0x21; // LED6 off time high byte

    // channel 7
    public static final int LED7_ON_LOW = 0x22; // LED7 on time low byte
    public static final int LED7_ON_HIGH = 0x23; // LED7 on time high byte
    public static final int LED7_OFF_LOW = 0x24; // LED7 off time low byte
    public static final int LED7_OFF_HIGH = 0x25; // LED7 off time high byte

    // channel 8
    public static final int LED8_ON_LOW = 0x26; // LED8 on time low byte
    public static final int LED8_ON_HIGH = 0x27; // LED8 on time high byte
    public static final int LED8_OFF_LOW = 0x28; // LED8 off time low byte
    public static final int LED8_OFF_HIGH = 0x29; // LED8 off time high byte

    // channel 9
    public static final int LED9_ON_LOW = 0x2A; // LED9 on time low byte
    public static final int LED9_ON_HIGH = 0x2B; // LED9 on time high byte
    public static final int LED9_OFF_LOW = 0x2C; // LED9 off time low byte
    public static final int LED9_OFF_HIGH = 0x2D; // LED9 off time high byte

    // channel 10
    public static final int LED10_ON_LOW = 0x2E; // LED10 on time low byte
    public static final int LED10_ON_HIGH = 0x2F; // LED10 on time high byte
    public static final int LED10_OFF_LOW = 0x30; // LED10 off time low byte
    public static final int LED10_OFF_HIGH = 0x31; // LED10 off time high byte

    // channel 11
    public static final int LED11_ON_LOW = 0x32; // LED11 on time low byte
    public static final int LED11_ON_HIGH = 0x33; // LED11 on time high byte
    public static final int LED11_OFF_LOW = 0x34; // LED11 off time low byte
    public static final int LED11_OFF_HIGH = 0x35; // LED11 off time high byte

    // channel 12
    public static final int LED12_ON_LOW = 0x36; // LED12 on time low byte
    public static final int LED12_ON_HIGH = 0x37; // LED12 on time high byte
    public static final int LED12_OFF_LOW = 0x38; // LED12 off time low byte
    public static final int LED12_OFF_HIGH = 0x39; // LED12 off time high byte

    // channel 13
    public static final int LED13_ON_LOW = 0x3A; // LED13 on time low byte
    public static final int LED13_ON_HIGH = 0x3B; // LED13 on time high byte
    public static final int LED13_OFF_LOW = 0x3C; // LED13 off time low byte
    public static final int LED13_OFF_HIGH = 0x3D; // LED13 off time high byte

    // channel 14
    public static final int LED14_ON_LOW = 0x3E; // LED14 on time low byte
    public static final int LED14_ON_HIGH = 0x3F; // LED14 on time high byte
    public static final int LED14_OFF_LOW = 0x40; // LED14 off time low byte
    public static final int LED14_OFF_HIGH = 0x41; // LED14 off time high byte

    // channel 15
    public static final int LED15_ON_LOW = 0x42; // LED15 on time low byte
    public static final int LED15_ON_HIGH = 0x43; // LED15 on time high byte
    public static final int LED15_OFF_LOW = 0x44; // LED15 off time low byte
    public static final int LED15_OFF_HIGH = 0x45; // LED15 off time high byte


    public static final int ALL_LED_ON_L = 0xFA; // load all the LED on low registers
    public static final int ALL_LED_ON_H = 0xFB; // load all the LED on high registers
    public static final int ALL_LED_OFF_L = 0xFC; // load all the LED off low registers
    public static final int ALL_LED_OFF_H = 0xFD; // load all the LED off high registers

    public static final int PRESCALE = 0xFE; // prescaler for output frequency

    public static final int TESTMODE = 0xFF; // defines the test mode to be entered

    // Mode 1 Bits
    public static final int MODE1_RESTART = 0x80; // 0 restart disabled, 1 restart enabled
    public static final int MODE1_EXTERNAL_CLOCK = 0x40; // 0 use internal clock, 1 use external clock pin
    public static final int MODE1_AUTO_INCREMENT = 0x20; // 0 auto increment disabled, 1 auto increment enabled
    public static final int MODE1_SLEEP = 0x10; // 0 normal mode, 1 sleep low power mode oscillator off
    public static final int MODE1_SUB1 = 0x08; // 0 does not respond to i2c sub address 1, 1 responds
    public static final int MODE1_SUB2 = 0x04; // 0 does not respond to i2c sub address 2, 1 responds
    public static final int MODE1_SUB3 = 0x02; //  0 does not respond to i2c sub address 3, 1 responds
    public static final int ALLCALL = 0x01; // 0 does not respond to led all call i2c address, 1 responds

    // mode 2 bits
    public static final int MODE2_INVERTED = 0x10; // 0 output logic state not inverted use when external driver is used, 1 state not inverted use when no external driver is used
    public static final int MODE2_OCH = 0x08; // 0 outputs change on STOP command, 1 outputs change on ACK command
    public static final int OUTDRV = 0x04; // 0 outputs configured with open drain, 1 outputs configured with totem pole

    /**
     * return the name of this device implementation
     *
     * @return
     */
    public String getDeviceName();

    /**
     * Write a single value to the register at address
     *
     * @param address of register
     * @param value   value to write to the register
     */
    public void writeRegister(int address, byte value) throws IOException;

    /**
     * Write multiple values to the device in one stream starting at register at startAddress
     * and incrementing the register address for each value
     *
     * @param startAddress start register address
     * @param values       array of bytes to write to registers starting with start address
     */
    public void writeRegisters(int startAddress, byte[] values) throws IOException;

    /**
     * Read the value from the specified address
     *
     * @param address
     * @return
     */
    public int readRegister(int address) throws IOException;

    /**
     * Read the values from multiple addresses starting at the start address
     * into the buffer provided
     * at the offset specified
     * and size number of bytes
     * in PCA9685 this requires auto incrementing registers
     *
     * @param startAddress the start register address
     * @return number of bytes read
     */
    public int readRegisters(int startAddress, byte[] buffer, int offset, int size) throws IOException;
}
