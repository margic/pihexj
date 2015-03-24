package com.margic.adafruitpwm;

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

    private Configuration configuration;

    @Before
    public void setUpServoDriver() {
        try {
            this.configuration = new PropertiesConfiguration("com.margic.pihex.properties");
        }catch(ConfigurationException ce){
            LOGGER.error("Failed to get properties.", ce);
        }
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
        assertEquals((byte)0x01, mockDevice.readRegister(PCA9685Device.MODE1));
        assertEquals((byte)0x04, mockDevice.readRegister(PCA9685Device.MODE2));
    }

}
