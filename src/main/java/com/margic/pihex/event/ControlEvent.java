package com.margic.pihex.event;

import java.util.Arrays;

/**
 * Created by paulcrofts on 4/1/15.
 * This event represents input from a control source
 * Control events can be sampled from remote control
 * demodulator and added to the bus as control events
 * <p>
 * The assignment of a channel depends on the mode the
 * robot is operating in. That is the reason there are
 * no names assigned to channel numbers.
 */
public class ControlEvent {

    public static int CHANNEL_MIN = 0;
    public static int CHANNEL_MAX = 1024;

    // channels for 6 channel rc radio
    public static int CHANNEL_0 = 0; // left stick x
    public static int CHANNEL_1 = 1; // left stick y
    public static int CHANNEL_2 = 2; // right stick x
    public static int CHANNEL_3 = 3; // right stick y
    public static int CHANNEL_4 = 4; // switch 1
    public static int CHANNEL_5 = 5; // switch 2

    private int[] inputChannels;

    /**
     * Create a new control input event object
     */
    public ControlEvent() {
        this.inputChannels = new int[6];
    }

    /**
     * Get all the input channel values in one array
     *
     * @return
     */
    public int[] getInputChannels() {
        return inputChannels;
    }

    /**
     * Set replace the input channel values with a new array
     * of values
     *
     * @param inputChannels
     */
    public void setInputChannels(int[] inputChannels) {
        this.inputChannels = inputChannels;
    }

    /**
     * Set the value of a specific input channel
     *
     * @param channel
     * @param value
     */
    public void setInputChannel(int channel, int value) {
        inputChannels[channel] = value;
    }

    /**
     * gets the value of a specific input channel
     *
     * @param channel
     * @return
     */
    public int getInputChannel(int channel) {
        return inputChannels[channel];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ControlEvent) {
            ControlEvent other = (ControlEvent) obj;
            return Arrays.equals(inputChannels, other.inputChannels);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inputChannels.length; i++) {
            stringBuilder.append("Channel-")
                    .append(i)
                    .append(": ")
                    .append(inputChannels[i])
                    .append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }
}
