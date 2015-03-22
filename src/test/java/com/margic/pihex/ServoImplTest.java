package com.margic.pihex;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by paulcrofts on 3/18/15.
 */
public class ServoImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServoImplTest.class);

    @Test
    public void testServoBuilder() {
        ServoImpl servo = new ServoImpl.Builder()
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

    @Test
    public void testGetServoPulse() {
        ServoImpl servo = new ServoImpl.Builder()
                .name("TestServo")
                .channel(0)
                .center(0)
                .build();

        int testAngle = -91;
        // testing exceeds
        LOGGER.info("testing servo getPulse exceeding min angle with {}", testAngle);
        assertEquals(1000, servo.getPulseLength(testAngle));
        testAngle = 91;
        LOGGER.info("testing servo getPulse exceeding max angle with {}", testAngle);
        assertEquals(2000, servo.getPulseLength(testAngle));
        testAngle = 0;
        LOGGER.info("testing servo getPulse angle {}", testAngle);
        assertEquals(1500, servo.getPulseLength(testAngle));

        // testing extremes
        testAngle = -90;
        LOGGER.info("testing servo getPulse exceeding min angle with {}", testAngle);
        assertEquals(1000, servo.getPulseLength(testAngle));
        testAngle = 90;
        LOGGER.info("testing servo getPulse exceeding max angle with {}", testAngle);
        assertEquals(2000, servo.getPulseLength(testAngle));

        // testing extremes
        testAngle = -89;
        LOGGER.info("testing servo getPulse exceeding min angle with {}", testAngle);
        assertEquals(1006, servo.getPulseLength(testAngle));
        testAngle = 89;
        LOGGER.info("testing servo getPulse exceeding max angle with {}", testAngle);
        assertEquals(1994, servo.getPulseLength(testAngle));
    }

}
