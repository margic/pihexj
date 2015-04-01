package com.margic.pihex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.api.Controller;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/25/15.
 * <p>
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
    private Controller controller;

    public void init(String[] args) {
        LOGGER.info("Initializing PiHex Service");
        injector = Guice.createInjector(new PihexModule());
        context = injector.getInstance(CamelContext.class);
    }

    public void start() {
        LOGGER.info("Startup PiHex Service");
        ServoDriver driver = injector.getInstance(ServoDriver.class);
        try {
            context.setAllowUseOriginalMessage(false);
            addRoutes(context);
            context.start();


        } catch (Exception e) {
            LOGGER.error("Error starting camel context", e);
        }
        controller = injector.getInstance(Controller.class);
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

    public void addRoutes(CamelContext context) throws Exception {
        context.addRoutes(new EventBusRouteBuilder());
    }
}
