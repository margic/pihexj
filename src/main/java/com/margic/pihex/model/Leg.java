package com.margic.pihex.model;

import com.margic.pihex.api.Servo;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by paulcrofts on 3/29/15.
 *
 * Object to represent a leg of the robot
 */
public class Leg {

    private Servo coxa;
    private Servo femur;
    private Servo tibia;

    private String name;

    public Leg(String name, Servo coxa, Servo femur, Servo tibia) {
        this.name = name;
        this.coxa = coxa;
        this.femur = femur;
        this.tibia = tibia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Servo getCoxa() {
        return coxa;
    }

    public void setCoxa(Servo coxa) {
        this.coxa = coxa;
    }

    public Servo getFemur() {
        return femur;
    }

    public void setFemur(Servo femur) {
        this.femur = femur;
    }

    public Servo getTibia() {
        return tibia;
    }

    public void setTibia(Servo tibia) {
        this.tibia = tibia;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Name", name)
                .append("Coxa angle", coxa.getAngle())
                .append("Femur angle", femur.getAngle())
                .append("Tibia angle", tibia.getAngle())
                .toString();
    }
}
