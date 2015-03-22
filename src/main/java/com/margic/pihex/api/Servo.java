package com.margic.pihex.api;

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
    public String getName();

    public int getChannel();

    public void setChannel(int channel);

    public void setName(String name);

    public int getAngle();

    public void setAngle(int angle);

    public int getPulseLength(int angle);

    /*
     *  Begin servo calibration methods
     */

    public int getRange();

    /**
     * Range is a physical constraint for a servo.
     * Servos can have hard stops that physically bind the servo within the range of motion
     * Typically 180 degs resulting in motion +/- 90degs
     * @param range
     */
    public void setRange(int range);

    public int getCenter();

    /**
     * Set the center position for the servo. This is the location the servo should go to when
     * told to center. To align servos it is necessary to be able to set the center to allow for
     * variances in installation
     * @param center
     */
    public void setCenter(int center);

    /**
     * In applications where the servo is moving a surface the output may need to be limited to avoid
     * the controlled surface binding. For example in RC aircraft a rudder may only be able to move +/- 30 deg.
     * moving a servo more than this would damage the controls.
     * @param limit
     */
    public void setLowLimit(int limit);
    /**
     * In applications where the servo is moving a surface the output may need to be limited to avoid
     * the controlled surface binding. For example in RC aircraft a rudder may only be able to move +/- 30 deg.
     * moving a servo more than this would damage the controls.
     * @param limit
     */
    public void setHighLimit(int limit);

    public int getLowLimit();

    public int getHighLimit();

    /*
     * End servo calibration methods
     */
}
