package com.margic.pihex.servo;

import com.margic.servo4j.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by paulcrofts on 3/15/15.
 */
public class Servo {
    private static final Logger log = LoggerFactory.getLogger(Servo.class);

    private ServoDriver driver;

    @Inject
    public Servo(ServoDriver driver){
        this.driver = driver;
    }
}
