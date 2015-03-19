package com.margic.pihex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/15/15.
 * The servo class represents a servo on the robot and maps name of servo to servo channel
 * The class contains calibration limits for the servo and provider angle to pulse calculation
 */
public class Servo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Servo.class);

    private static final int MIN_PULSE = 1000;
    private static final int MAX_PULSE = 2000;

    private String name;
    private int channel;
    private int maxPulse = MAX_PULSE;
    private int minPulse = MIN_PULSE;
    private int angle;
    private int center;

    private Servo(){}

    public String getName() {
        return name;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPulse() {
        return maxPulse;
    }

    public int getMinPulse() {
        return minPulse;
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

    public void getPulseLength(int angle) {
        this.angle = angle;
        getPulseLength();
    }

    public void getPulseLength() {
        LOGGER.debug("Calculating pulse lenght for servo angle {}", angle);
    }

    public static class Builder{
        private Servo newServo = new Servo();
        public Builder channel(int channel){
            newServo.channel = channel;
            return this;
        }
        public Builder name(String name){
            newServo.name = name;
            return this;
        }
        public Builder center(int center){
            newServo.center = center;
            return this;
        }
        public Builder angle(int angle){
            newServo.angle = angle;
            return this;
        }
        public Servo build(){
            return newServo;
        }
    }

}
