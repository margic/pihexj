package com.margic.pihex.event;

/**
 * Created by paulcrofts on 4/1/15.
 * This event represents input from a control source
 *
 */
public class ControlEvent {

    private int[] inputChannels;

    public int[] getInputChannels() {
        return inputChannels;
    }

    public void setInputChannels(int[] inputChannels) {
        this.inputChannels = inputChannels;
    }
}
