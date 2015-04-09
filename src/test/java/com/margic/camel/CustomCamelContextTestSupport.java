package com.margic.camel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.PihexModule;
import com.margic.pihex.camel.context.CustomCamelContext;
import org.apache.camel.CamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.configuration.Configuration;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/26/15.
 * <p>
 * Provides a camel context for testing that is built the same way as the
 * real context. From the module
 */
public class CustomCamelContextTestSupport extends CamelTestSupport {

    protected static final Logger log = LoggerFactory.getLogger(CustomCamelContextTestSupport.class);

    protected Injector injector = Guice.createInjector(new PihexModule());
    protected Configuration config;

    private Executor executor;

    @Override
    protected CamelContext createCamelContext() throws Exception {

        // create camel context without injecting optional startup from pihexmodule
        Registry registry = injector.getInstance(Registry.class);
        this.config = injector.getInstance(Configuration.class);

        return new CustomCamelContext(registry, config);
    }

    /**
     * This method sets the value of a configuration property to allow
     * setting and overriding properties. If not used then properties will come
     * from the src/test/resources properties file
     *
     * @param name
     * @param value
     */
    protected void setConfigurationProperty(String name, String value) {
        Configuration config = context().getRegistry().lookupByNameAndType("configuration", Configuration.class);
        log.info("Setting config value name: {}, current value: {}, new value: {}", name, config.getString(name), value);
        config.setProperty(name, value);
    }

    /**
     * gets an executor for the http fluent client
     * This executor configures the client to avoid caching sockets
     * other wise test cases where the context is replace cause errors
     * @return
     */
    protected Executor getExecutor(){
        if (executor == null){
            executor = Executor.newInstance(HttpClients.custom().disableConnectionState().build());
        }
        return executor;
    }

}
