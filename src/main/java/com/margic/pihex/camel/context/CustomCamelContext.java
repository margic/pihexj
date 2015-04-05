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
        setTracing(config.getBoolean("com.margic.pihex.camel.tracing", false));
        PropertiesComponent pc = getComponent("properties", PropertiesComponent.class);
        pc.addFunction(new ConfigurationPropertiesFunction(config));
    }

    /**
     * Provides a way to add a start listener to kick off the
     * post start events. Injected as an optional binding so the
     * test cases can start the camel context without triggering off
     * the extra post starup events.
     * @param listener
     * @throws Exception
     */
    @Inject(optional = true)
    @Override
    public void addStartupListener(org.apache.camel.StartupListener listener) throws Exception {
        super.addStartupListener(listener);
    }
}
