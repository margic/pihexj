package com.margic.camel;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by paulcrofts on 3/26/15.
 */
public class PiHexCamelTestSupport extends CamelTestSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(PiHexCamelTestSupport.class);


    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        return super.useOverridePropertiesWithPropertiesComponent();
    }
}
