package com.margic.pihex.model;

import com.margic.pihex.api.Servo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paulcrofts on 4/2/15.
 */
@XmlRootElement
public class ServoConfig {

    private static final Logger log = LoggerFactory.getLogger(ServoConfig.class);

    public static final int DEFAULT_RANGE = 180;
    public static final int MIN_PULSE = 1000;
    public static final int MAX_PULSE = 2000;

    private int channel;
    // range 180 -90 = 1000 90 = 2000 0 = 1500 0-90 = 500
    private int range;
    private int center;
    private int lowLimit = Integer.MIN_VALUE; // can't initilize to 0, 0 is a valid limit
    private int highLimit = Integer.MAX_VALUE; // can't initilize to 0, 0 is a valid limit
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getRange() {
        if(range == 0){
            return DEFAULT_RANGE;
        }else {
            return range;
        }
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
        if(lowLimit == Integer.MIN_VALUE){
            // return default
            return 0 - (getRange() / 2);
        }
        return lowLimit;
    }

    public void setLowLimit(int lowLimit) {
        this.lowLimit = lowLimit;
    }

    public int getHighLimit() {
        if(highLimit == Integer.MAX_VALUE){
            // return default
            return 0 + (getRange() / 2);
        }
        return highLimit;
    }

    public void setHighLimit(int highLimit) {
        this.highLimit = highLimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("channel", channel)
                .append("range", range)
                .append("center", center)
                .append("lowlimit", lowLimit)
                .append("highLimit", highLimit)
                .build();
    }

    public static class Builder {
        private ServoConfig newConfig = new ServoConfig();

        public Builder channel(int channel) {
            newConfig.channel = channel;
            return this;
        }

        public Builder name(String name){
            newConfig.name = name;
            return this;
        }

        public Builder range(int range){
            newConfig.range = range;
            return this;
        }

        public Builder center(int center){
            newConfig.center = center;
            return this;
        }

        public Builder lowLimit(int lowLimit){
            newConfig.lowLimit = lowLimit;
            return this;
        }

        public Builder highLimit(int highLimit){
            newConfig.highLimit = highLimit;
            return this;
        }

        public ServoConfig build() {
            return newConfig;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 91)
                .append(name)
                .append(channel)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ServoConfig rhs = (ServoConfig) obj;
        return new EqualsBuilder()
                .append(channel, rhs.channel)
                .append(name, rhs.name)
                .append(center, rhs.center)
                .append(lowLimit, rhs.lowLimit)
                .append(highLimit, rhs.highLimit)
                .isEquals();
    }
}
