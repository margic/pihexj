package com.margic.adafruitpwm;

import com.margic.adafruitpwm.mock.MockPCA9685Device;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/17/15.
 */
public class AdafruitServoDriverTest {


    @Test
    public void testGetPreScale(){
        PCA9685Device device = new MockPCA9685Device();

        AdafruitServoDriver driver = new AdafruitServoDriver(device);

        byte prescale = driver.getPreScale(200);

        assertEquals(30, prescale);
    }


}
