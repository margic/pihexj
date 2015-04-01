package com.margic.pihex;

import com.google.common.eventbus.EventBus;
import com.margic.pihex.api.Controller;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.model.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by paulcrofts on 3/29/15.
 */
public class PiHexController implements Controller{

    private static final Logger log = LoggerFactory.getLogger(PiHexController.class);

    private Body body;
    private ServoDriver driver;

    @Inject
    private EventBus bus;

    @Inject
    public PiHexController(ServoDriver driver){
        this.body = new Body();
        try {
            body.updatePositions(driver);
        }catch (IOException ioe){
            log.error("Error setting initial positions", ioe);
        }
    }

    public EventBus getBus() {
        return bus;
    }

    public void setBus(EventBus bus) {
        this.bus = bus;
    }
}
