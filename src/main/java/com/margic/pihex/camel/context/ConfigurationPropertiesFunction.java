package com.margic.pihex.camel.context;

import org.apache.camel.component.properties.PropertiesFunction;
import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;

/**
 * Created by paulcrofts on 3/30/15.
 */
public class ConfigurationPropertiesFunction implements PropertiesFunction {

    private static final String NAME = "config";
    private Configuration config;

    @Inject
    public ConfigurationPropertiesFunction(Configuration config){
        this.config = config;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String apply(String remainder) {
        return config.getString(remainder);
    }
}
