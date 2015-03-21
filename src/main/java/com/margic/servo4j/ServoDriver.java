package com.margic.servo4j;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/15/15.
 */
public interface ServoDriver {

    public static final String PWM_FREQUENCY_PROP = "com.margic.pwm.frequency";
    public static final int DEFAULT_PWM_FREQUENCY = 50;

    /**
     * @return String name of servo driver implementation
     */
    public String getDriverName();

    /**
     * set the pwm interval typically every 20ms is typical
     * for a servo this corresponds to 50hz
     *
     * @param frequency the frequency in hertz
     */
    public void setPWMFrequency(int frequency) throws IOException;

    /**
     * sets the pulse length for a servo
     *
     * @param pulseLength
     */
    public void setPulse(int channel, int pulseLength) throws IOException;

    public void init() throws IOException;

}
