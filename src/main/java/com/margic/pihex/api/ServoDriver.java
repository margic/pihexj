package com.margic.pihex.api;

import java.io.IOException;
import java.util.List;

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
     * sets the pulse length for a single servo
     * assumes a servo is designated to a some sort of channel identifier.
     * Driver implementer should provid mapping for servo channel id to
     * implementation
     *
     * @param servo an implementation of the servo interface
     */
    public void updateServo(Servo servo) throws IOException;


    /**
     * updates a list of servos in a single operation
     * @param servos
     * @throws IOException
     */
    public void updateServos(List<Servo> servos) throws IOException;


    /**
     * Hook to initialize the servo driver
     * @throws IOException
     */
    public void init() throws IOException;



}
