package com.margic.adafruitpwm;

import com.margic.adafruitpwm.mock.MockPCA9685Device;
import com.margic.servo4j.ServoDriver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/17/15.
 */
public class AdafruitServoDriverTest {

    private AdafruitServoDriver driver;
    private MockPCA9685Device mockDevice;

    @Before
    public void setUpServoDriver(){
        mockDevice = new MockPCA9685Device();
        this.driver = new AdafruitServoDriver(mockDevice);
    }

    @Test
    public void testGetPreScale(){
        byte prescale = driver.getPreScale(200);

        assertEquals(30, prescale);
    }

    @Test
    public void testInitDevice(){

        mockDevice.dumpRegisters();
        mockDevice.dumpByteStream();
    }

}
