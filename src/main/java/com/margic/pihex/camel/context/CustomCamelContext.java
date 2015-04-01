package com.margic.pihex.camel.context;

import com.google.inject.Inject;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/27/15.
 */
public class CustomCamelContext extends DefaultCamelContext {

    private static final Logger log = LoggerFactory.getLogger(CustomCamelContext.class);

    @Inject
    public CustomCamelContext(Registry registry, Configuration config) {
        super(registry);
        log.info("Creating Guice Camel Context");
        PropertiesComponent pc = getComponent("properties", PropertiesComponent.class);
        pc.addFunction(new ConfigurationPropertiesFunction(config));
    }
}
