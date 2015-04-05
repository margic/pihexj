package com.margic.pihex;

import com.margic.pihex.api.Controller;
import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.model.Body;
import com.margic.pihex.model.Leg;
import com.margic.pihex.model.ServoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by paulcrofts on 3/29/15.
 */

public class PiHexController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(PiHexController.class);

    private Body body;
    private ServoDriver driver;

    @Inject
    public PiHexController(Body body, ServoDriver driver) {
        this.body = body;
        this.driver = driver;
    }


    @Override
    public void handleControlEvent(ControlEvent event) {
        log.debug("Received control event {}", event);
    }

    @Override
    public Servo getServo(int channel){
        return body.getServo(channel);
    }


    @Override
    public void handleServoConfigUpdateEvent(ServoConfig servoConfig) {
        Servo servo = body.getServo(servoConfig.getChannel());
        servo.setServoConfig(servoConfig);
        try {
            driver.updateServo(servo);
        }catch(IOException ioe){
            log.error("Unable to update servo {}", servo, ioe);
        }
    }

    /**
     * Call this method to send the current positions to
     * the physical servos using the driver
     */
    @Override
    public void updateAllServos(){
        Leg[] legs = body.getLegs();
        try {
            for (Leg leg : legs) {
                driver.updateServo(leg.getCoxa());
                driver.updateServo(leg.getFemur());
                driver.updateServo(leg.getTibia());
            }
        }catch(Exception ioe){
            log.error("Unable to update servo angles.", ioe);
        }
    }


}
