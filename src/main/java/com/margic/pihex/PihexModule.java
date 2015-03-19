package com.margic.pihex;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.margic.adafruitpwm.AdaPCA9685Device;
import com.margic.adafruitpwm.AdafruitServoDriver;
import com.margic.servo4j.ServoDriver;
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
        bind(ServoDriver.class).to(AdafruitServoDriver.class);


    }

    @Singleton
    @Provides
    public Configuration provideConfiguration(){
        try{
            Configuration config = new PropertiesConfiguration("com.margic.pihex.properties");
            return config;
        }catch (ConfigurationException ce){
            log.error("Unable to get configuration ", ce);
        }
        return null;
    }

    @Singleton
    @Provides
    public ServoDriver providerServoDriver(Configuration config){
        I2CDevice device = null;
        try {
            I2CBus bus = I2CFactory.getInstance(config.getInt(I2C_BUS_PROP, 1));
            device = bus.getDevice(config.getInt(I2C_ADDRESS_PROP, 0x40));
        }catch (IOException ioe){
            log.error("Unable to get I2CDevice", ioe);
            return null;
        }
        AdaPCA9685Device pca9685Device = new AdaPCA9685Device(device);
        AdafruitServoDriver servoDriver = new AdafruitServoDriver(pca9685Device);
        return servoDriver;
    }
}
