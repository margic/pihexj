package com.margic.pihex.api;

import com.margic.pihex.event.ServoUpdateEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by paulcrofts on 3/15/15.
 */
public interface ServoDriver {

    static final String PWM_FREQUENCY_PROP = "com.margic.pwm.frequency";
    static final int DEFAULT_PWM_FREQUENCY = 50;

    /**
     * @return String name of servo driver implementation
     */
    String getDriverName();

    /**
     * set the pwm interval typically every 20ms is typical
     * for a servo this corresponds to 50hz
     *
     * @param frequency the frequency in hertz
     */
    void setPulseFrequency(int frequency) throws IOException;

    /**
     * sets the pulse length for a single servo
     * assumes a servo is designated to a some sort of channel identifier.
     * Driver implementer should provide mapping for servo channel id to
     * implementation
     * Does not write to device until flush is called
     *
     * This allows updates to be staged to allow optimizing of writes to servos
     *
     * @param servo an implementation of the servo interface
     */
    void updateServo(ServoUpdateEvent servo) throws IOException;

    /**
     * flushes updates to servo devices
     * @return the number of servos updated
     * @throws IOException
     */
    int flush() throws IOException;

    /**
     * Hook to initialize the servo driver
     *
     * @throws IOException
     */
     void init() throws IOException;


}
