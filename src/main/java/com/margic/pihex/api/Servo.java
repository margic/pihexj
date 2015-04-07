package com.margic.pihex.api;

import com.margic.pihex.model.ServoConfig;

/**
 * Interface to expose servo parameters
 *
 * Servos require calibration depending on installation position.
 * Values like center depend on servo wheel fitment
 * range is limited by physical servo
 * limits depend on installation
 *
 * This interface provides the methods to fine tune individual servo parameters
 * allowing each servo implementation to define it's own calibration
 *
 * Servo drivers use the getPulseLength method to retrieve a corrected pulse length
 * to drive the actual servo with.
 *
 * All callibtation limits are set in degrees
 *
 * Created by paulcrofts on 3/22/15.
 */
public interface Servo {

    ServoConfig getServoConfig();

    void setServoConfig(ServoConfig servoConfig);

//    int getAngle();
//
//    void setAngle(int angle);

    int getPulseLength(int angle);
}
