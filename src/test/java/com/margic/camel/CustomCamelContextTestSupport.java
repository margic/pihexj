package com.margic.camel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.PihexModule;
import org.apache.camel.CamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/26/15.
 *
 * Provides a camel context for testing that is built the same way as the
 * real context. From the module
 *
 */
public class CustomCamelContextTestSupport extends CamelTestSupport {

    private static final Logger log = LoggerFactory.getLogger(CustomCamelContextTestSupport.class);

    protected Injector injector = Guice.createInjector(new PihexModule());

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return injector.getInstance(CamelContext.class);
    }

    /**
     * This method sets the value of a configuration property to allow
     * setting and overriding properties. If not used then properties will come
     * from the src/test/resources properties file
     * @param name
     * @param value
     */
    protected void setConfigurationProperty(String name, String value){
        Configuration config = context().getRegistry().lookupByNameAndType("configuration", Configuration.class);
        log.info("Setting config value name: {}, current value: {}, new value: {}", name, config.getString(name), value);
        config.setProperty(name, value);
    }
}
