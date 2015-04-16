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
        this.driver = new AdafruitServoDriver(new PCA9685Device[]{mockDevice}, true);
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
        assertEquals(0x20, mockDevice.readRegister(PCA9685Device.MODE1));
        assertEquals(0x04, mockDevice.readRegister(PCA9685Device.MODE2));
    }

    /**
     * this test tests writing a single servo flush then writing a second servo
     * @throws IOException
     */
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


    /**
     * This test test buffering more than one servo update the flushing. Will
     * leave a channel unset that should result in zero's
     * @throws IOException
     */
    @Test
    public void testMultipleUpdateServo() throws IOException{
        Servo servo0 = new ServoImpl(new ServoConfig.Builder()
                .channel(0)
                .center(0)
                .build());
        Servo servo2 = new ServoImpl(new ServoConfig.Builder()
                .channel(2)
                .center(0)
                .build());

        driver.updateServo(new ServoUpdateEvent(servo0, 0));
        driver.updateServo(new ServoUpdateEvent(servo2, 90));
        driver.flush();

        // assert servo 0 is set
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_HIGH));
        assertEquals(0x33, mockDevice.readRegister(PCA9685Device.LED0_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED0_OFF_HIGH));

        // assert servo 1 was not set
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_HIGH));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_OFF_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_OFF_HIGH));

        // assert servo 2 is set
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_HIGH));
        assertEquals(0x9A, mockDevice.readRegister(PCA9685Device.LED2_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED2_OFF_HIGH));
    }

    /**
     * testing setting a value on channel 1 flushing and
     * then setting 0 and 2 on either side. channel 1 should be set to cached value
     * @throws IOException
     */
    @Test
    public void testCachedServoUpdate() throws IOException{
        Servo servo0 = new ServoImpl(new ServoConfig.Builder()
                .center(0)
                .channel(0)
                .build());

        Servo servo1 = new ServoImpl(new ServoConfig.Builder()
                .center(0)
                .channel(1)
                .build());

        Servo servo2 = new ServoImpl(new ServoConfig.Builder()
                .center(0)
                .channel(2)
                .build());
        driver.updateServo(new ServoUpdateEvent(servo1, 0));
        driver.flush();

        // assert servo 1 is set
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_HIGH));
        assertEquals(0x33, mockDevice.readRegister(PCA9685Device.LED1_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED1_OFF_HIGH));


        // not set 0 and 2
        driver.updateServo(new ServoUpdateEvent(servo0, 90));
        driver.updateServo(new ServoUpdateEvent(servo2, 90));
        driver.flush();

        mockDevice.dumpRegisters();

        //0
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_HIGH));
        assertEquals(0x9A, mockDevice.readRegister(PCA9685Device.LED0_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED0_OFF_HIGH));

        //1
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED1_ON_HIGH));
        assertEquals(0x33, mockDevice.readRegister(PCA9685Device.LED1_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED1_OFF_HIGH));

        //2
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
