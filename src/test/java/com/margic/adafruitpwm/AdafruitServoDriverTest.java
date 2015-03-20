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
        this.driver = new AdafruitServoDriver(configuration, mockDevice);
    }

    @Test
    public void testGetPreScale() throws Exception{
        int prescale = driver.getPreScale(200);
        assertEquals(30, prescale);

        prescale = driver.getPreScale(30);
        assertEquals(202, prescale);
    }

    @Test
    public void testSetPWMFrequency() throws IOException{
        driver.setPWMFrequency(50);
        mockDevice.dumpRegisters();
        assertEquals(121, mockDevice.readRegister(PCA9685Device.PRESCALE));

        driver.setPWMFrequency(200);
        mockDevice.dumpRegisters();
        assertEquals(30, mockDevice.readRegister(PCA9685Device.PRESCALE));

        driver.setPWMFrequency(30);
        mockDevice.dumpRegisters();
        assertEquals(202, mockDevice.readRegister(PCA9685Device.PRESCALE));

        mockDevice.dumpByteStream();
    }

//    @Test
//    public void testInitDevice() {
//        driver.initDevice();
//        mockDevice.dumpRegisters();
//        assertEquals(121, mockDevice.readRegister(PCA9685Device.PRESCALE));
//        // test that the frequency can be set by configuration
//        configuration.setProperty(AdafruitServoDriver.PWM_FREQUENCY_PROP, "100");
//        driver.initDevice();
//        assertEquals(60, mockDevice.readRegister(PCA9685Device.PRESCALE));
//        mockDevice.dumpRegisters();
//        mockDevice.dumpByteStream();
//    }

}
