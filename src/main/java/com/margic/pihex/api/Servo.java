package com.margic.pihex.api;

/**
 * Created by paulcrofts on 3/22/15.
 */
public interface Servo {
    String getName();

    int getChannel();

    void setChannel(int channel);

    void setName(String name);

    int getMaxPulse();

    int getMinPulse();

    int getRange();

    void setRange(int range);

    int getAngle();

    void setAngle(int angle);

    int getCenter();

    void setCenter(int center);

    int getPulseLength(int angle);

    int getPulseLength();
}
