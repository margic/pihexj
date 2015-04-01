package com.margic.camel.route;

import com.margic.camel.PiHexCamelTestSupport;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/27/15.
 */
public class EventBusRouteBuilderTest extends PiHexCamelTestSupport {

    private static final Logger log = LoggerFactory.getLogger(EventBusRouteBuilderTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new EventBusRouteBuilder();
    }

    @Test
    public void testEventBusRoute(){
        log.info("Testing event route builder");
    }

}
