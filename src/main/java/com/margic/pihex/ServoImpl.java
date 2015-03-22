package com.margic.pihex;

import com.margic.pihex.api.Servo;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/15/15.
 * The servo class represents a servo on the robot and maps name of servo to servo channel
 * The class contains calibration limits for the servo and provider angle to pulse calculation
 *
 * The servo is intended to provide an abstraction between the user of a servo who
 * should be thinking in terms of setting servo angles and the driver that should be
 * setting servo pulses.
 *
 * Please view the test to see examples of pulse output based on calibration values
 * @See com.margic.pihex.ServoImplTest
 */
public class ServoImpl implements com.margic.pihex.api.Servo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServoImpl.class);

    private static final int DEFAULT_RANGE = 180;
    private static final int MIN_PULSE = 1000;
    private static final int MAX_PULSE = 2000;


    private String name;
    private int channel;
    // range 180 -90 = 1000 90 = 2000 0 = 1500 0-90 = 500
    private int range;
    private int angle;
    private int center;
    private int lowLimit = Integer.MIN_VALUE; // can't initilize to 0, 0 is a valid limit
    private int highLimit = Integer.MAX_VALUE; // can't initilize to 0, 0 is a valid limit

    private ServoImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getRange() {
        if (range == 0) {
            setRange(DEFAULT_RANGE);
        }
        return range;
    }

    @Override
    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public int getAngle() {
        return angle;
    }

    /**
     * In this implementation it is possible to set an angle that exceeds both the limits
     * and the range. Rather than throwing exceptions this will be allowed but the get
     * pulseLength method will return a min/max value allowed by range and limits if this
     * is the case.
     * @param angle
     */
    @Override
    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public int getCenter() {
        return center;
    }

    @Override
    public void setCenter(int center) {
        this.center = center;
    }

    /**
     * Returns the pulseLength for a give angle for this servo with it's limits
     * @param argAngle typically call this method with getAngle() as the argument for argAngle this ensures the angle is stored in the servo
     * @return
     */
    @Override
    public int getPulseLength(int argAngle) {

        // limit angle
        if(argAngle > getHighLimit()){
            int newAngle = getHighLimit();
            LOGGER.trace("Angle exceeds high limit. Requested: {}, Using: {}", argAngle, newAngle);
            argAngle = newAngle;
        }
        if(argAngle < getLowLimit()){
            int newAngle = getLowLimit();
            LOGGER.trace("Angle exceeds low limit. Requested: {}, Using: {}", argAngle, newAngle);
            argAngle = newAngle;

        }

        double offset = getMicrosPerDeg() * (argAngle + getCenter());

        double mid = (MIN_PULSE + MAX_PULSE) / 2;

        int pulse = (int) Math.round(mid + offset);

        if (pulse < MIN_PULSE) {
            LOGGER.trace("specified angle exceeds minimum, returning minimum instead");
            pulse = MIN_PULSE;
        }
        if (pulse > MAX_PULSE) {
            LOGGER.trace("specified angle exceeds maximum, returning maximum instead");
            pulse = MAX_PULSE;
        }
        LOGGER.trace("Calculating pulse length for servo angle {}: {}\u00B5S", argAngle, pulse);
        return pulse;
    }

    @Override
    public void setLowLimit(int limit) {
        this.lowLimit = limit;
    }

    @Override
    public void setHighLimit(int limit) {
        this.highLimit = limit;
    }

    @Override
    public int getLowLimit(){
        if(lowLimit == Integer.MIN_VALUE){
            // return default
            return 0 - (getRange() / 2);
        }
        return lowLimit;
    }

    @Override
    public int getHighLimit(){
        if(highLimit == Integer.MAX_VALUE){
            // return default
            return 0 + (getRange() / 2);
        }
        return highLimit;
    }

    private double getMicrosPerDeg() {
        double perDeg = (double) (MAX_PULSE - MIN_PULSE) / (double) getRange();
        LOGGER.debug("µS/deg: {} for range: {}", perDeg, getRange());
        return perDeg;
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(151, 181).
                append(name).
                append(channel).
                toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("channel", channel).
                append("angle", angle).
                append("range", range).
                append("center", center).
                append("lowLimit", lowLimit).
                append("highLimit", highLimit).
                toString();
    }

    public static class Builder {
        private ServoImpl newServo = new ServoImpl();

        public Builder channel(int channel) {
            newServo.channel = channel;
            return this;
        }

        public Builder name(String name) {
            newServo.name = name;
            return this;
        }

        public Builder center(int center) {
            newServo.center = center;
            return this;
        }

        public Builder angle(int angle) {
            newServo.angle = angle;
            return this;
        }

        public Builder range(int range) {
            newServo.range = range;
            return this;
        }

        public Builder highLimit(int limit){
            newServo.highLimit = limit;
            return this;
        }

        public Builder lowLimit(int limit){
            newServo.lowLimit = limit;
            return this;
        }

        public Servo build() {
            return newServo;
        }
    }

}
