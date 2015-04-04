package com.margic.pihex.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paulcrofts on 4/2/15.
 */
@XmlRootElement
public class ServoCalibration {

    private int channel;
    private int range;
    private int center;
    private int lowLimit;
    private int highLimit;


    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getCenter() {
        return center;
    }

    public void setCenter(int center) {
        this.center = center;
    }

    public int getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(int lowLimit) {
        this.lowLimit = lowLimit;
    }

    public int getHighLimit() {
        return highLimit;
    }

    public void setHighLimit(int highLimit) {
        this.highLimit = highLimit;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this)
                .append("channel", channel)
                .append("range", range)
                .append("center", center)
                .append("lowlimit", lowLimit)
                .append("highLimit", highLimit)
                .build();
    }
}
