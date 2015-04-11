package com.margic.pihex.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by paulcrofts on 4/9/15.
 */
@XmlRootElement
public class Move {

    private int startAngle;
    private int endAngle;
    private int channel;

    public Move(){

    }
}
