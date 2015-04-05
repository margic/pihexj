package com.margic.pihex.api;

import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.model.ServoConfig;

/**
 * Created by paulcrofts on 3/29/15.
 */
public interface Controller {
    /**
     * process an event with a change to the controller input
     * @param event
     */
    void handleControlEvent(ControlEvent event);

    /**
     * return the servo assigned to channel
     * @param channel
     * @return
     */
    Servo getServo(int channel);


    /**
     * Updates the servo specified by the servo config and returns that servo
     * @param servoConfig
     * @return
     */
    Servo handleServoConfigUpdateEvent(ServoConfig servoConfig);

    /**
     * this method updates a single servos physical position
     * @param servo
     */
    void handleUpdateServoEvent(Servo servo);

}
