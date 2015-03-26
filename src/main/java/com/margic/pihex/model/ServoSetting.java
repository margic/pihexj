package com.margic.pihex.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paulcrofts on 3/26/15.
 * This model object is used to encapsulte servo setting
 */
@XmlRootElement
public class ServoSetting {

    private int servoChannel;
    private int servoAngle;

    public int getServoChannel() {
        return servoChannel;
    }

    public void setServoChannel(int servoChannel) {
        this.servoChannel = servoChannel;
    }

    public int getServoAngle() {
        return servoAngle;
    }

    public void setServoAngle(int servoAngle) {
        this.servoAngle = servoAngle;
    }
}
