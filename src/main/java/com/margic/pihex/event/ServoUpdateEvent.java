package com.margic.pihex.event;

import com.margic.pihex.api.Servo;

/**
 * Created by paulcrofts on 4/1/15.
 */
public class ServoUpdateEvent {

    private Servo servo;

    public ServoUpdateEvent(Servo servo){
        this.servo = servo;
    }

    public Servo getServo() {
        return servo;
    }

    public void setServo(Servo servo) {
        this.servo = servo;
    }
}
