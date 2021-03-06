package com.margic.pihex;

import com.margic.pihex.api.Servo;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.model.ServoConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by paulcrofts on 3/18/15.
 */
public class ServoImplTest {

    private static final Logger log = LoggerFactory.getLogger(ServoImplTest.class);

    @Test
    public void testServoBuilder() {
        Servo servo = new ServoImpl(new ServoConfig.Builder()
                        .center(10)
                        .channel(1)
                        .name("Test Name")
                        .range(120)
                        .lowLimit(-110)
                        .highLimit(111)
                        .build());

        // test the builder built the object correctly
        assertEquals(10, servo.getServoConfig().getCenter());
        assertEquals(120, servo.getServoConfig().getRange());
        assertEquals(-110, servo.getServoConfig().getLowLimit());
        assertEquals(111, servo.getServoConfig().getHighLimit());
        assertEquals("Test Name", servo.getServoConfig().getName());
        assertEquals(1, servo.getServoConfig().getChannel());

        // set all the values
        servo.getServoConfig().setName("Test1");
        servo.getServoConfig().setCenter(20);
        servo.getServoConfig().setChannel(2);
        servo.getServoConfig().setRange(140);
        servo.getServoConfig().setLowLimit(-100);
        servo.getServoConfig().setHighLimit(101);

        // test the setters
        assertEquals(20, servo.getServoConfig().getCenter());
        assertEquals("Test1", servo.getServoConfig().getName());
        assertEquals(2, servo.getServoConfig().getChannel());
        assertEquals(140, servo.getServoConfig().getRange());
        assertEquals(-100, servo.getServoConfig().getLowLimit());
        assertEquals(101, servo.getServoConfig().getHighLimit());

    }

    /**
     * Returns a servo with no angle set for testing pulse lengths
     * @return
     */
    private Servo getTestServo(){
        return new ServoImpl(new ServoConfig.Builder()
                        .name("TestServo")
                        .channel(0)
                        .center(0)
                        .build());
    }

    private void assertPulse(ServoUpdateEvent servoUpdate, int expectedPulseLength){

        log.info("Testing pulse for servo: {}", servoUpdate);
        int pulse = servoUpdate.getPulseLength();
        log.info("Servo pulse length: {}", pulse);
        assertEquals(expectedPulseLength, pulse);
    }

    @Test
    public void testGetPulseLength(){
        Servo servo = getTestServo();

        // TESTING CENTER
        log.info("Testing CENTER");
        assertPulse(new ServoUpdateEvent(servo, 0), 1500);

        log.info("Testing CENTER calibrated to +5");
        servo = getTestServo(); // resets the servo to new object
        servo.getServoConfig().setCenter(5);
        assertPulse(new ServoUpdateEvent(servo, 0), 1528);

        log.info("Testing CENTER a lower range of 120 center");
        servo = getTestServo(); // resets the servo to new object
        servo.getServoConfig().setCenter(0);
        servo.getServoConfig().setRange(120);
        assertPulse(new ServoUpdateEvent(servo, 0), 1500);

        log.info("Testing CENTER a lower range of 120 center calibrated to + 5");
        servo = getTestServo(); // resets the servo to new object
        servo.getServoConfig().setCenter(5);
        servo.getServoConfig().setRange(120);
        assertPulse(new ServoUpdateEvent(servo, 0), 1542);

        // TESTING MAX
        log.info("Testing MAX");
        servo = getTestServo();
        assertPulse(new ServoUpdateEvent(servo, 90), 2000);

        log.info(("Testing MAX beyond range"));
        servo = getTestServo();
        assertPulse(new ServoUpdateEvent(servo, 91), 2000);

        log.info(("Testing MAX beyond limit within range"));
        servo = getTestServo();
        servo.getServoConfig().setHighLimit(80);
        assertPulse(new ServoUpdateEvent(servo, 90), 1944);


        // TESTING MIN
        log.info("Testing MIN");
        servo = getTestServo();
        assertPulse(new ServoUpdateEvent(servo, -90), 1000);

        log.info(("Testing MIN beyond range"));
        servo = getTestServo();
        assertPulse(new ServoUpdateEvent(servo, -91), 1000);

        log.info(("Testing MIN beyond limit within range"));
        servo = getTestServo();
        servo.getServoConfig().setLowLimit(-80);
        assertPulse(new ServoUpdateEvent(servo, -90), 1056);
    }
}
