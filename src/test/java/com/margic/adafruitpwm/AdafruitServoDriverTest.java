package com.margic.adafruitpwm;

import com.margic.pihex.ServoImpl;
import com.margic.pihex.api.Servo;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.model.ServoConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by paulcrofts on 3/17/15.
 */
public class AdafruitServoDriverTest {

    private static Logger log = LoggerFactory.getLogger(AdafruitServoDriverTest.class);

    private AdafruitServoDriver driver;
    private MockPCA9685Device mockDevice;

    @Before
    public void setUpServoDriver() {
        mockDevice = new MockPCA9685Device("Mock Device");
        this.driver = new AdafruitServoDriver(new PCA9685Device[]{mockDevice});
    }

    @Test
    public void testGetPreScale() throws Exception{
        int prescale = driver.getPreScale(200);
        assertEquals(33, prescale);

        prescale = driver.getPreScale(30);
        assertEquals(226, prescale);
    }

    @Test
    public void testSetPWMFrequency() throws IOException{
        driver.setPulseFrequency(50);
        mockDevice.dumpRegisters();
        assertEquals(135, mockDevice.readRegister(PCA9685Device.PRESCALE));

        driver.setPulseFrequency(200);
        mockDevice.dumpRegisters();
        assertEquals(33, mockDevice.readRegister(PCA9685Device.PRESCALE));

        driver.setPulseFrequency(30);
        mockDevice.dumpRegisters();
        assertEquals(226, mockDevice.readRegister(PCA9685Device.PRESCALE));

        mockDevice.dumpByteStream();
    }

    @Test
    public void testInit() throws IOException{
        driver.init();
        mockDevice.dumpRegisters();
        mockDevice.dumpByteStream();
        assertEquals(0x11, mockDevice.readRegister(PCA9685Device.MODE1));
        assertEquals(0x04, mockDevice.readRegister(PCA9685Device.MODE2));
    }

    @Test
    public void testUpdateServo() throws IOException{
        Servo servo = new ServoImpl(new ServoConfig.Builder()
                        .center(0)
                        .build());

        driver.updateServo(new ServoUpdateEvent(servo, 0));
        driver.flush();
        mockDevice.dumpRegisters();

        // assert led0 set correct
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_HIGH));
        assertEquals(0x33, mockDevice.readRegister(PCA9685Device.LED0_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED0_OFF_HIGH));

        servo.getServoConfig().setChannel(2);

        driver.updateServo(new ServoUpdateEvent(servo, 90));
        driver.flush();

        // assert led2 set correct
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_HIGH));
        assertEquals(0x9A, mockDevice.readRegister(PCA9685Device.LED2_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED2_OFF_HIGH));
    }

    @Test
    public void testGetDeviceOffset() throws Exception{
        assertEquals(0, driver.getDeviceOffset(0));
        assertEquals(0, driver.getDeviceOffset(1));
        assertEquals(0, driver.getDeviceOffset(14));
        assertEquals(0, driver.getDeviceOffset(15));
        assertEquals(16, driver.getDeviceOffset(16));
        assertEquals(16, driver.getDeviceOffset(17));
        assertEquals(16, driver.getDeviceOffset(30));
        assertEquals(16, driver.getDeviceOffset(31));
        assertEquals(32, driver.getDeviceOffset(32));
    }
}
