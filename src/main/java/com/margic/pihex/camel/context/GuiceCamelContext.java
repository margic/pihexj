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
public class GuiceCamelContext extends DefaultCamelContext{

    private static final Logger log = LoggerFactory.getLogger(GuiceCamelContext.class);


    private Configuration config;

    @Inject
    public GuiceCamelContext(Configuration config, Registry registry){
        log.info("Creating Guice Camel Context");
        this.config = config;
        super.setRegistry(registry);
    }

    @Inject
    public void setConfigurationPropertiesParser(Configuration configuration){
        log.info("Setting properties parser");
        PropertiesComponent pc = super.getComponent("properties", PropertiesComponent.class);
        pc.setPropertiesParser(new ConfigurationPropertiesParser(configuration));
    }

}
