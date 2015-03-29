package com.margic.camel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.margic.pihex.PihexModule;
import org.apache.camel.CamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by paulcrofts on 3/26/15.
 *
 * Provides a camel context for testing that is built the same way as the
 * real context. From the module
 *
 */
public class PiHexCamelTestSupport extends CamelTestSupport {

    private static final Logger log = LoggerFactory.getLogger(PiHexCamelTestSupport.class);

    private Injector injector = Guice.createInjector(new PihexModule());

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return injector.getInstance(CamelContext.class);
    }
}
