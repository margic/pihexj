package com.margic.pihex;

import com.margic.pihex.api.Controller;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.model.Body;
import com.margic.pihex.model.ServoCalibration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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
    public void processControlEvent(ControlEvent event) {
        log.debug("Received control event {}", event);
    }

    @Override
    public void processServoCalibrationUpdate(ServoCalibration servoCalibration) {
        log.debug("Received servo calibration event {}", servoCalibration);
    }
}
