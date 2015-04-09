package com.margic.pihex.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paulcrofts on 4/6/15.
 */
@XmlRootElement
public class ServoUpdate {

    private int channel;
    private int angle;

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("channel", channel)
                .append("angle", angle)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ServoUpdate rhs = (ServoUpdate) obj;
        return new EqualsBuilder()
                .append(channel, rhs.channel)
                .append(angle, rhs.angle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 97)
                .append(channel)
                .append(angle)
                .toHashCode();
    }
}
