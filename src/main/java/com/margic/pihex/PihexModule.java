package com.margic.pihex;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.margic.adafruitpwm.AdaPCA9685Device;
import com.margic.adafruitpwm.AdafruitServoDriver;
import com.margic.adafruitpwm.MockPCA9685Device;
import com.margic.adafruitpwm.PCA9685Device;
import com.margic.pihex.api.ServoDriver;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
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
    private static final Logger log = LoggerFactory.getLogger(PihexModule.class);

    public static final String I2C_BUS_PROP = "com.margic.pihex.i2cbus";
    public static final String I2C_ADDRESS_PROP = "com.margic.pihex.i2caddress";

    @Override
    protected void configure() {


    }

    @Singleton
    @Provides
    public Configuration provideConfiguration() {
        try {
            Configuration config = new PropertiesConfiguration("com.margic.pihex.properties");
            return config;
        } catch (ConfigurationException ce) {
            log.error("Unable to get configuration ", ce);
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
        if(config.getBoolean("com.margic.pihex.useMock", false)){
            pca9685Device = new MockPCA9685Device();
        }else {
            I2CDevice device = null;
            try {
                I2CBus bus = I2CFactory.getInstance(config.getInt(I2C_BUS_PROP, 1));
                device = bus.getDevice(config.getInt(I2C_ADDRESS_PROP, 0x40));
            } catch (IOException ioe) {
                log.error("Unable to get I2CDevice", ioe);
                return null;
            }
            pca9685Device = new AdaPCA9685Device(device);
        }
        AdafruitServoDriver servoDriver = new AdafruitServoDriver(config, pca9685Device);
        return servoDriver;
    }
}
