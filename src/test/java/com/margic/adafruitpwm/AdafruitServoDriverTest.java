package com.margic.adafruitpwm;

import com.margic.servo4j.PCA9685Device;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/17/15.
 */
public class AdafruitServoDriverTest {


    @Test
    public void testGetPreScale(){
        PCA9685Device device = new PCA9685Device() {
            @Override
            public String getDeviceName() {
                return "Test Device";
            }

            @Override
            public void init() {

            }

            @Override
            public void setAllServos(int[] servoPulseArray) {

            }
        };


        AdafruitServoDriver driver = new AdafruitServoDriver(device);

        byte prescale = driver.getPreScale(200);

        assertEquals(30, prescale);
    }



}
