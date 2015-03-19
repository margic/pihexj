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

    private static final int MIN_PULSE = 1000; // micro seconds
    private static final int MAX_PULSE = 2000; // micro seconds
    private static final int DEFAULT_RANGE = 180;

    // range 180 -90 = 1000 90 = 2000 0 = 1500 0-90 = 500

    private String name;
    private int channel;
    private int maxPulse = MAX_PULSE;
    private int minPulse = MIN_PULSE;
    private int range;
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

    public int getRange() {
        if(range == 0){
            setRange(DEFAULT_RANGE);
        }
        return range;
    }

    public void setRange(int range){
        this.range = range;
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

    public int getPulseLength(int angle) {
        this.angle = angle;
        return getPulseLength();
    }

    public int getPulseLength() {
        double offset = getMicrosPerDeg() * getAngle();

        double mid = (getMinPulse() + getMaxPulse()) / 2;

        int pulse = (int)Math.round(mid + offset);

        if(pulse < getMinPulse()){
            LOGGER.debug("specified angle exceeds minimum, returning minimum instead");
            pulse = getMinPulse();
        }
        if(pulse > getMaxPulse()){
            LOGGER.debug("specified angle exceeds maximum, returning maximum instead");
            pulse = getMaxPulse();
        }
        LOGGER.debug("Calculating pulse length for servo angle {}: {}\u00B5S", angle, pulse);
        return pulse;
    }

    private double getMicrosPerDeg(){
        double perDeg = (double)(getMaxPulse() - getMinPulse()) / (double)getRange();
        LOGGER.debug("µS/deg: {}", perDeg);
        return perDeg;
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
