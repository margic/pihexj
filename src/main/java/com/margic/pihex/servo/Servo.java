package com.margic.pihex.servo;

import com.margic.servo4j.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by paulcrofts on 3/15/15.
 * The servo class provides access to a specific servo via a driver
 * The class contains calibration limits for the servo and provider angle to pulse calculation
 */
public class Servo {
    private static final Logger log = LoggerFactory.getLogger(Servo.class);

    // dependency
    private ServoDriver driver;

    private String name;
    private int maxPulse;
    private int minPulse;
    private int angle;
    private int center;


    @Inject
    public Servo(ServoDriver driver){
        this.driver = driver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPulse() {
        return maxPulse;
    }

    public void setMaxPulse(int maxPulse) {
        this.maxPulse = maxPulse;
    }

    public int getMinPulse() {
        return minPulse;
    }

    public void setMinPulse(int minPulse) {
        this.minPulse = minPulse;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getCenter() {
        return center;
    }

    public void setCenter(int center) {
        this.center = center;
    }
}
