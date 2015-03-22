package com.margic.pihex;

import com.margic.pihex.api.Servo;
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
        Servo servo = new ServoImpl.Builder()
                .center(10)
                .channel(1)
                .name("Test Name")
                .angle(90)
                .range(120)
                .lowLimit(-110)
                .highLimit(111)
                .build();

        // test the builder built the object correctly
        assertEquals(90, servo.getAngle());
        assertEquals(10, servo.getCenter());
        assertEquals(120, servo.getRange());
        assertEquals(-110, servo.getLowLimit());
        assertEquals(111, servo.getHighLimit());
        assertEquals("Test Name", servo.getName());
        assertEquals(1, servo.getChannel());

        // set all the values
        servo.setName("Test1");
        servo.setAngle(45);
        servo.setCenter(20);
        servo.setChannel(2);
        servo.setRange(140);
        servo.setLowLimit(-100);
        servo.setHighLimit(101);

        // test the setters
        assertEquals(45, servo.getAngle());
        assertEquals(20, servo.getCenter());
        assertEquals("Test1", servo.getName());
        assertEquals(2, servo.getChannel());
        assertEquals(140, servo.getRange());
        assertEquals(-100, servo.getLowLimit());
        assertEquals(101, servo.getHighLimit());

    }

    /**
     * Returns a servo with no angle set for testing pulse lengths
     * @return
     */
    private Servo getTestServo(){
        return new ServoImpl.Builder()
                .name("TestServo")
                .channel(0)
                .center(0)
                .build();
    }

    private void assertPulse(Servo testServo, int expectedPulseLength){

        LOGGER.info("Testing pulse for servo: {}", testServo);
        int pulse = testServo.getPulseLength(testServo.getAngle());
        LOGGER.info("Servo pulse length: {}", pulse);
        assertEquals(expectedPulseLength, pulse);
    }

    @Test
    public void testGetPulseLength(){
        Servo servo = getTestServo();

        // TESTING CENTER
        LOGGER.info("Testing CENTER");
        servo.setAngle(0);
        assertPulse(servo, 1500);

        LOGGER.info("Testing CENTER calibrated to +5");
        servo = getTestServo(); // resets the servo to new object
        servo.setCenter(5);
        servo.setAngle(0);
        assertPulse(servo, 1528);

        LOGGER.info("Testing CENTER a lower range of 120 center");
        servo = getTestServo(); // resets the servo to new object
        servo.setCenter(0);
        servo.setRange(120);
        servo.setAngle(0);
        assertPulse(servo, 1500);

        LOGGER.info("Testing CENTER a lower range of 120 center calibrated to + 5");
        servo = getTestServo(); // resets the servo to new object
        servo.setCenter(5);
        servo.setRange(120);
        servo.setAngle(0);
        assertPulse(servo, 1542);

        // TESTING MAX
        LOGGER.info("Testing MAX");
        servo = getTestServo();
        servo.setAngle(90);
        assertPulse(servo, 2000);

        LOGGER.info(("Testing MAX beyond range"));
        servo = getTestServo();
        servo.setAngle(91);
        assertPulse(servo, 2000);

        LOGGER.info(("Testing MAX beyond limit within range"));
        servo = getTestServo();
        servo.setAngle(90);
        servo.setHighLimit(80);
        assertPulse(servo, 1944);


        // TESTING MIN
        LOGGER.info("Testing MIN");
        servo = getTestServo();
        servo.setAngle(-90);
        assertPulse(servo, 1000);

        LOGGER.info(("Testing MIN beyond range"));
        servo = getTestServo();
        servo.setAngle(-91);
        assertPulse(servo, 1000);

        LOGGER.info(("Testing MIN beyond limit within range"));
        servo = getTestServo();
        servo.setAngle(-90);
        servo.setLowLimit(-80);
        assertPulse(servo, 1056);


    }
}
