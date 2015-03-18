package com.margic.adafruitpwm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/17/15.
 */
public class AdafruitServoDriverTest {


    @Test
    public void testGetPreScale(){
        AdafruitServoDriver driver = new AdafruitServoDriver(null);

        byte prescale = driver.getPreScale(200);

        assertEquals(30, prescale);
    }



}
