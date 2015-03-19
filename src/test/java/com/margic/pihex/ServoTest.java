package com.margic.pihex;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 3/18/15.
 */
public class ServoTest {

    @Test
    public void testServoBuilder(){
        Servo servo = new Servo.Builder()
                .center(10)
                .channel(1)
                .name("Test Name")
                .angle(90)
                .build();

        // test the builder built the object correctly
        assertEquals(90, servo.getAngle());
        assertEquals(10, servo.getCenter());
        assertEquals("Test Name", servo.getName());
        assertEquals(1, servo.getChannel());

        // set all the values
        servo.setName("Test1");
        servo.setAngle(45);
        servo.setCenter(20);
        servo.setChannel(2);

        // test the setters
        assertEquals(45, servo.getAngle());
        assertEquals(20, servo.getCenter());
        assertEquals("Test1", servo.getName());
        assertEquals(2, servo.getChannel());

    }

}
