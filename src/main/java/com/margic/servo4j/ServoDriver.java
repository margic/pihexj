package com.margic.servo4j;

/**
 * Created by paulcrofts on 3/15/15.
 */
public interface ServoDriver {

    /**
     * @return String name of servo driver implementation
     */
    public String getDriverName();

    /**
     * set the pwm interval typically every 20ms is typical
     * for a servo this corresponds to 50hz
     * @param frequency the frequency in hertz
     */
    public void setPWMFrequency(int frequency);
}
