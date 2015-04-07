package com.margic.pihex.event;

import com.margic.pihex.api.Servo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by paulcrofts on 4/1/15.
 */
public class ServoUpdateEvent {

    private Servo servo;
    private int angle;

    public ServoUpdateEvent(Servo servo, int angle){
        this.servo = servo;
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Servo getServo() {
        return servo;
    }

    public void setServo(Servo servo) {
        this.servo = servo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("servo", servo)
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
        ServoUpdateEvent rhs = (ServoUpdateEvent) obj;
        return new EqualsBuilder()
                .append(servo, rhs.servo)
                .append(angle, rhs.angle)
                .isEquals();
    }
}
