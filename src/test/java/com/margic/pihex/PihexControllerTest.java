package com.margic.pihex;

import com.margic.adafruitpwm.AdafruitServoDriver;
import com.margic.adafruitpwm.MockPCA9685Device;
import com.margic.pihex.api.Controller;
import com.margic.pihex.api.Servo;
import com.margic.pihex.model.Body;
import com.margic.pihex.model.ServoCalibration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class PihexControllerTest {

    private Controller controller;

    @Before
    public void before(){
        controller = new PiHexController(new Body(), new AdafruitServoDriver(new MockPCA9685Device()));
    }

    @Test
    public void getServoTest() throws Exception {

        Servo servo = controller.getServo(0);

        // make sure it returns the correct servo
        assertEquals(0, servo.getChannel());

        servo = controller.getServo(6);
        assertEquals(6, servo.getChannel());
    }

    @Test
    public void testUpdateServoCalibration() throws Exception {
        ServoCalibration calibration = new ServoCalibration();
        calibration.setCenter(1);
        calibration.setHighLimit(80);
        calibration.setLowLimit(-80);
        calibration.setRange(190);

        controller.updateServoCalibration(0, calibration);

        Servo servo = controller.getServo(0);

        // validate calibration
        assertEquals(1, servo.getCenter());
        assertEquals(80, servo.getHighLimit());
        assertEquals(-80, servo.getLowLimit());
        assertEquals(190, servo.getRange());

        assertEquals(0, servo.getAngle());

        assertEquals(1505, servo.getPulseLength(0));
        assertEquals(1926, servo.getPulseLength(80));
        // test past limit
        assertEquals(1926, servo.getPulseLength(85));
    }

    @After
    public void after(){
        controller = null;
    }
}
