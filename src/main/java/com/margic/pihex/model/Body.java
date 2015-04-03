package com.margic.pihex.model;

import com.margic.pihex.ServoImpl;
import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/29/15.
 *
 * The body object has legs like the actual robot.
 */
public class Body {

    private Leg[] legs;

    public Body(){
        this.legs = new Leg[6];

        for(int i = 0; i < legs.length; i++){
            Servo coxa = new ServoImpl.Builder()
                    .name("Leg " + i + " Coxa")
                    .channel(i * 3 + 0)
                    .build();
            Servo femur = new ServoImpl.Builder()
                    .name("Leg " + i + " Femur")
                    .channel(i * 3 + 1)
                    .build();
            Servo tibia = new ServoImpl.Builder()
                    .name("Leg " + i + " Tibia")
                    .channel(i * 3 + 2)
                    .build();
            legs[i] = new Leg("Leg_" + i, coxa, femur, tibia);
        }
    }

    public Leg[] getLegs(){ return legs; }

    public Leg getLeg(int leg){
        return legs[leg];
    }

    /**
     * return the servo assigned to the channel
     * @param channel
     * @return
     */
    public Servo getServo(int channel){
        // servos are assigned to servos on start up
        int leg = channel / 3; // 3 servos per leg
        int servo = channel % 3;
        switch(servo){
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

    /**
     * Call this method to send the current positions to
     * the physical servos using the driver
     */
    public void updatePositions(ServoDriver driver) throws IOException{
        for(Leg leg: legs){
            driver.updateServo(leg.getCoxa());
            driver.updateServo(leg.getFemur());
            driver.updateServo(leg.getTibia());
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
