package com.margic.pihex.api;

import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.model.ServoCalibration;

/**
 * Created by paulcrofts on 3/29/15.
 */
public interface Controller {
    /**
     * process an event with a change to the controller input
     * @param event
     */
    void processControlEvent(ControlEvent event);

    /**
     * return the servo assigned to channel
     * @param channel
     * @return
     */
    Servo getServo(int channel);


    void updateServoCalibration(int channel, ServoCalibration servoCalibration);

    /**
     * This method updates the physical angle of all servos
     */
    void updateAllServos();
}
