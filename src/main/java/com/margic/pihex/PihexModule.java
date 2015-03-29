package com.margic.pihex;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.margic.adafruitpwm.AdaPCA9685Device;
import com.margic.adafruitpwm.AdafruitServoDriver;
import com.margic.adafruitpwm.MockPCA9685Device;
import com.margic.adafruitpwm.PCA9685Device;
import com.margic.pihex.api.ServoDriver;
import com.margic.pihex.camel.context.GuiceCamelContext;
import com.margic.pihex.camel.context.GuiceRegistry;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.spi.Registry;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;


/**
 * Created by paulcrofts on 3/15/15.
 */
public class PihexModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(PihexModule.class);

    public static final String I2C_BUS_PROP = "com.margic.pihex.i2cbus";
    public static final String I2C_ADDRESS_PROP = "com.margic.pihex.i2caddress";

    /**
     * Guice configure method. make sure to bind any beans required for camel before creating the
     * registry. The registry will use the injector to find beans annotated for use by camel
     */
    @Override
    protected void configure() {

        bind(EventBus.class).annotatedWith(Names.named("eventBus")).toInstance(new EventBus());
        bind(Registry.class).to(GuiceRegistry.class).asEagerSingleton();
        bind(CamelContext.class).to(GuiceCamelContext.class).asEagerSingleton();
    }

    @Singleton
    @Provides
    public Configuration provideConfiguration() {
        try {
            Configuration config = new PropertiesConfiguration("com.margic.pihex.properties");
            return config;
        } catch (ConfigurationException ce) {
            LOGGER.error("Unable to get configuration ", ce);
        }
        return null;
    }

    @Singleton
    @Provides
    public ServoDriver provideServoDriver(Configuration config) {
        /*
            Putting this in here to load mock based on property for convenience
            will figure out better way later
         */
        PCA9685Device pca9685Device = null;
        if (config.getBoolean("com.margic.pihex.useMock", false)) {
            pca9685Device = new MockPCA9685Device();
        } else {
            I2CDevice device = null;
            try {
                I2CBus bus = I2CFactory.getInstance(config.getInt(I2C_BUS_PROP, 1));
                device = bus.getDevice(config.getInt(I2C_ADDRESS_PROP, 0x40));
            } catch (IOException ioe) {
                LOGGER.error("Unable to get I2CDevice", ioe);
                return null;
            }
            pca9685Device = new AdaPCA9685Device(device);
        }
        AdafruitServoDriver servoDriver = new AdafruitServoDriver(pca9685Device);

        try {
            int pwmFreq = config.getInt(ServoDriver.PWM_FREQUENCY_PROP, ServoDriver.DEFAULT_PWM_FREQUENCY);
            servoDriver.init();
            servoDriver.setPulseFrequency(pwmFreq);
        } catch (IOException ioe) {
            LOGGER.error("Failed to start servo driver", ioe);
        }
        return servoDriver;
    }
}
