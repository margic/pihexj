package com.margic.pihex.model;

import com.margic.pihex.ServoImpl;
import com.margic.pihex.api.Servo;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by paulcrofts on 3/29/15.
 * <p>
 * The body object has legs like the actual robot.
 */
public class Body {

    private Leg[] legs;

    public Body() {
        this.legs = new Leg[6];

        // the body inializes the legs with default servo configs if any are loaded from file after startup the config will be replaced.
        for (int i = 0; i < legs.length; i++) {
            Servo coxa = new ServoImpl(new ServoConfig.Builder()
                            .channel(i * 3 + 0)
                            .name("Leg " + i + " Coxa")
                            .build());
            Servo femur = new ServoImpl(new ServoConfig.Builder()
                            .channel(i * 3 + 1)
                            .name("Leg " + i + " Femur")
                            .build());

            Servo tibia = new ServoImpl(new ServoConfig.Builder()
                            .channel(i * 3 + 2)
                            .name("Leg " + i + " Tibia")
                            .build());
            legs[i] = new Leg("Leg_" + i, coxa, femur, tibia);
        }
    }

    public Leg[] getLegs() {
        return legs;
    }

    public Leg getLeg(int leg) {
        return legs[leg];
    }

    /**
     * return the servo assigned to the channel
     *
     * @param channel
     * @return
     */
    public Servo getServo(int channel) {
        // servos are assigned to servos on start up
        int leg = channel / 3; // 3 servos per leg
        int servo = channel % 3;
        switch (servo) {
            case 0:
                return legs[leg].getCoxa();
            case 1:
                return legs[leg].getFemur();
            case 2:
                return legs[leg].getTibia();
            default:
                return null;
        }
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Leg0", legs[0])
                .append("Leg1", legs[1])
                .append("Leg2", legs[2])
                .append("Leg3", legs[3])
                .append("Leg4", legs[4])
                .append("Leg5", legs[5])
                .toString();
    }
}
