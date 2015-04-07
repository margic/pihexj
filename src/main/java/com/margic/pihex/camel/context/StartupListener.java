package com.margic.pihex.camel.context;

import com.google.common.eventbus.EventBus;
import com.margic.pihex.event.StartupEvent;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 4/4/15.
 *
 * This listener is used to provide a hook to kick of routes via the event bus that
 * need to be run when the context starts and the robot is ready to initialize
 */
public class StartupListener implements org.apache.camel.StartupListener {
    private static final Logger log = LoggerFactory.getLogger(StartupListener.class);

    @Override
    public void onCamelContextStarted(CamelContext context, boolean alreadyStarted) throws Exception {
        log.info("Camel startup listener, alreadyStarted: {}", alreadyStarted);
        if(!alreadyStarted){
            log.debug("Initial Startup Trigger Event on Event Bus");
            EventBus eventBus = context.getRegistry().lookupByNameAndType("eventBus", EventBus.class);
            if(eventBus == null){
                throw new Exception("Failed to lookup eventbus in camel registry. Can't initialize properly");
            }
            eventBus.post(new StartupEvent());
        }
    }
}
