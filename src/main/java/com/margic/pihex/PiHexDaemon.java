package com.margic.pihex;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/25/15.
 *
 * Setting this up for apache daemon
 * Write a Class (MyClass) that implements the following methods:
 * void init(String[] arguments): Here open configuration files, create a trace file, create ServerSockets, Threads
 * void start(): Start the Thread, accept incoming connections
 * void stop(): Inform the Thread to terminate the run(), close the ServerSockets
 * void destroy(): Destroy any object created in init()
 */
public class PiHexDaemon {

    private static final Logger LOGGER = LoggerFactory.getLogger(PiHexDaemon.class);
    private Injector injector;
    private CamelContext context;

    public void init(String[] args) {
        LOGGER.info("Initializing PiHex Service");
        injector = Guice.createInjector(new PihexModule());
        // note this below is not how to use an injector. don't type. this is bad
        SimpleRegistry registry = (SimpleRegistry)injector.getInstance(Registry.class);
        EventBus eventBus = new EventBus("com.margic.pihex.EventBus");
        registry.put("eventBus", eventBus);

        context = injector.getInstance(CamelContext.class);

    }

    public void start() {
        LOGGER.info("Startup PiHex Service");
        ServoDriver driver = injector.getInstance(ServoDriver.class);
        try {
            context.setAllowUseOriginalMessage(false);
            context.start();
        } catch (Exception e) {
            LOGGER.error("Error starting camel context", e);
        }

//        try {
//            LOGGER.info("setting test servo on");
//            Servo servo = new ServoImpl.Builder()
//                    .center(0)
//                    .channel(0)
//                    .name("test servo")
//                    .angle(-90)
//                    .range(180)
//                    .build();
//
//            driver.updateServo(servo);
//        } catch (IOException ioe) {
//            LOGGER.error("error", ioe);
//        }
    }

    public void stop() {
        LOGGER.info("Shutdown PiHex Service");
        try {
            context.stop();
        } catch (Exception e) {
            LOGGER.error("Failed to stop camel context");
        }
    }

    public void destroy() {
        LOGGER.info("Destroy PiHex Service");
        context = null;
    }
}
