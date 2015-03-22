package com.margic.pihex.api;

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
    public void setPulseFrequency(int frequency) throws IOException;

    /**
     * sets the pulse length for a servo
     * assumes a servo is designated to a some sort of channel identifier.
     * Driver implementer should provid mapping for servo channel id to
     * implementation
     *
     * @param pulseLength
     */
    public void setPulseLength(int channel, int pulseLength) throws IOException;





    /**
     * Hook to initialize the servo driver
     * @throws IOException
     */
    public void init() throws IOException;



}
