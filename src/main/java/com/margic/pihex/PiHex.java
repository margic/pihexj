package com.margic.pihex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.api.Servo;
import com.margic.pihex.api.ServoDriver;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by paulcrofts on 3/15/15.
 */
public class PiHex {
    private static final Logger LOGGER = LoggerFactory.getLogger(PiHex.class);

    public static void main(String... args) {
        LOGGER.info("Starting PiHex application");
        Injector injector = Guice.createInjector(new PihexModule());

        Configuration config = injector.getInstance(Configuration.class);

        ServoDriver driver = injector.getInstance(ServoDriver.class);
        int pwmFreq = config.getInt(ServoDriver.PWM_FREQUENCY_PROP, ServoDriver.DEFAULT_PWM_FREQUENCY);

        try {
            driver.init();
            driver.setPulseFrequency(pwmFreq);
            LOGGER.info("setting test servo on");
            Servo servo = new ServoImpl.Builder()
                    .center(0)
                    .channel(1)
                    .name("test servo")
                    .angle(0)
                    .range(180)
                    .build();
            driver.updateServo(servo);

        }catch (IOException ioe){
            LOGGER.error("error", ioe);
        }

    }
}
