package com.margic.pihex;

import com.margic.pihex.api.Servo;
import com.margic.pihex.model.ServoConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/15/15.
 * The servo class represents a servo on the robot and maps name of servo to servo channel
 * The class contains calibration limits for the servo and provider angle to pulse calculation
 * <p>
 * The servo is intended to provide an abstraction between the user of a servo who
 * should be thinking in terms of setting servo angles and the driver that should be
 * setting servo pulses.
 * <p>
 * Please view the test to see examples of pulse output based on calibration values
 *
 * @See com.margic.pihex.ServoImplTest
 */
public class ServoImpl implements com.margic.pihex.api.Servo {
    private static final Logger log = LoggerFactory.getLogger(ServoImpl.class);

    private ServoConfig servoConfig;
    private int angle;

    private ServoImpl() {
    }

    @Override
    public ServoConfig getServoConfig() {
        return servoConfig;
    }

    @Override
    public void setServoConfig(ServoConfig config) {
        this.servoConfig = config;
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
     *
     * @param angle
     */
    @Override
    public void setAngle(int angle) {
        this.angle = angle;
    }

    /**
     * Returns the pulseLength for a give angle for this servo with it's limits
     *
     * @param argAngle typically call this method with getAngle() as the argument for argAngle this ensures the angle is stored in the servo
     * @return
     */
    @Override
    public int getPulseLength(int argAngle) {
        // limit angle
        if (argAngle > servoConfig.getHighLimit()) {
            int newAngle = servoConfig.getHighLimit();
            log.trace("Angle exceeds high limit. Requested: {}, Using: {}", argAngle, newAngle);
            argAngle = newAngle;
        }
        if (argAngle < servoConfig.getLowLimit()) {
            int newAngle = servoConfig.getLowLimit();
            log.trace("Angle exceeds low limit. Requested: {}, Using: {}", argAngle, newAngle);
            argAngle = newAngle;
        }
        double offset = getMicrosPerDeg() * (argAngle + servoConfig.getCenter());
        double mid = (ServoConfig.MIN_PULSE + ServoConfig.MAX_PULSE) / 2;
        int pulse = (int) Math.round(mid + offset);
        if (pulse < ServoConfig.MIN_PULSE) {
            log.trace("specified angle exceeds minimum, returning minimum instead");
            pulse = ServoConfig.MIN_PULSE;
        }
        if (pulse > ServoConfig.MAX_PULSE) {
            log.trace("specified angle exceeds maximum, returning maximum instead");
            pulse = ServoConfig.MAX_PULSE;
        }
        log.trace("Calculating pulse length for servo angle {}: {}\u00B5S", argAngle, pulse);
        return pulse;
    }

    private double getMicrosPerDeg() {
        double perDeg = (double) (ServoConfig.MAX_PULSE - ServoConfig.MIN_PULSE) / (double) servoConfig.getRange();
        log.debug("µS/deg: {} for range: {}", perDeg, servoConfig.getRange());
        return perDeg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("angle", angle).
                append("servoConfig", servoConfig.toString()).
                toString();
    }

    public static class Builder {
        private ServoImpl newServo = new ServoImpl();

        public Builder servoConfig(ServoConfig servoConfig) {
            newServo.servoConfig = servoConfig;
            return this;
        }

        public Builder angle(int angle) {
            newServo.angle = angle;
            return this;
        }

        public Servo build() {
            return newServo;
        }
    }

}
