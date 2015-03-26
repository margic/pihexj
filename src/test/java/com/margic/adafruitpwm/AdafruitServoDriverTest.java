package com.margic.adafruitpwm;

import com.margic.pihex.ServoImpl;
import com.margic.pihex.api.Servo;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

    private static Logger LOGGER = LoggerFactory.getLogger(AdafruitServoDriverTest.class);

    private AdafruitServoDriver driver;
    private MockPCA9685Device mockDevice;

    @Before
    public void setUpServoDriver() {
        mockDevice = new MockPCA9685Device();
        this.driver = new AdafruitServoDriver(mockDevice);
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
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.MODE1));
        assertEquals(0x04, mockDevice.readRegister(PCA9685Device.MODE2));
    }

    @Test
    public void testUpdateServo() throws IOException{
        Servo servo = new ServoImpl.Builder()
                .angle(0)
                .channel(0)
                .build();
        driver.updateServo(servo);
        mockDevice.dumpRegisters();

        // assert led0 set correct
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED0_ON_HIGH));
        assertEquals(0x33, mockDevice.readRegister(PCA9685Device.LED0_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED0_OFF_HIGH));

        servo.setChannel(2);
        servo.setAngle(90);
        driver.updateServo(servo);

        // assert led2 set correct
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_LOW));
        assertEquals(0x00, mockDevice.readRegister(PCA9685Device.LED2_ON_HIGH));
        assertEquals(0x9A, mockDevice.readRegister(PCA9685Device.LED2_OFF_LOW));
        assertEquals(0x01, mockDevice.readRegister(PCA9685Device.LED2_OFF_HIGH));

    }
}
