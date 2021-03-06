package com.margic.pihex;

import com.margic.pihex.api.Controller;
import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.event.FlushServoUpdateEvent;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.model.Body;
import com.margic.pihex.model.ServoConfig;
import com.margic.pihex.model.ServoUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by paulcrofts on 3/29/15.
 */

public class PiHexController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(PiHexController.class);

    private Body body;
    private ServoDriver driver;

    @Inject
    public PiHexController(Body body, ServoDriver driver) {
        this.body = body;
        this.driver = driver;
    }


    @Override
    public void handleControlEvent(ControlEvent event) {
        log.debug("Received control event {}", event);
    }

    @Override
    public Servo getServo(int channel) {
        return body.getServo(channel);
    }


    @Override
    public Servo handleServoConfigUpdateEvent(ServoConfig servoConfig) {
        Servo servo = body.getServo(servoConfig.getChannel());
        servo.setServoConfig(servoConfig);
        return servo;
    }

    @Override
    public ServoUpdateEvent handlePostUpdateServo(ServoUpdate servoUpdate) {
        Servo servoToUpdate = getServo(servoUpdate.getChannel());
        ServoUpdateEvent updateEvent = new ServoUpdateEvent(servoToUpdate, servoUpdate.getAngle());
        return updateEvent;
    }

    /**
     * We don't call the implementation of the handleUpdateServoEvent directly
     * we invoke this by posting a servo object ot the eventbus
     * This allows the system to manage the queuing of requests to move servos
     * and handle dequeuing of the events one by one. The driver cannot process
     * more than one event at the same time as the interface to the device is a
     * serial device.
     *
     * @param servoUpdate
     */
    @Override
    public void handleUpdateServoEvent(ServoUpdateEvent servoUpdate) {
        try {
            driver.updateServo(servoUpdate);
        } catch (IOException ioe) {
            log.error("Failed to update servo {}", servoUpdate, ioe);
        }
    }

    @Override
    public int flushServoUpdates(FlushServoUpdateEvent servoUpdateEvent) {
        int updateCount = 0;
        try {
            updateCount += driver.flush();
        } catch (IOException ioe) {
            log.error("Failed to flush servo updates");
        }
        return updateCount;
    }
}
