package com.margic.pihex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.servo.Servo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/15/15.
 */
public class PiHex {
    private static final Logger log = LoggerFactory.getLogger(PiHex.class);

    public static void main(String... args){
        log.info("Starting PiHex application");
        Injector injector = Guice.createInjector(new PihexModule());

        Servo servo = injector.getInstance(Servo.class);

    }
}
