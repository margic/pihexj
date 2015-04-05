package com.margic.model;

import com.margic.pihex.api.Servo;
import com.margic.pihex.model.Body;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class BodyTest {

    @Test
    public void testGetServo() throws Exception{
        Body body = new Body();

        Servo servo = body.getServo(0);
        assertEquals(0, servo.getServoConfig().getChannel());

        servo = body.getServo(3);
        assertEquals(3, servo.getServoConfig().getChannel());

        servo = body.getServo(17);
        assertEquals(17, servo.getServoConfig().getChannel());
    }
}
