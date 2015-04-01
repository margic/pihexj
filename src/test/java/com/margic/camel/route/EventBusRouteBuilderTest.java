package com.margic.camel.route;

import com.google.common.eventbus.EventBus;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import com.margic.pihex.event.ControlEvent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by paulcrofts on 3/27/15.
 * <p>
 * The eventbus route is used to receive event and send them for processing depending on type of
 * event. This test test sending events to the bus directly to test the route.
 */
public class EventBusRouteBuilderTest extends CustomCamelContextTestSupport {

    private static final Logger log = LoggerFactory.getLogger(EventBusRouteBuilderTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        // set the config prop that has the endpoint uri using parent setter
        setConfigurationProperty("com.margic.pihex.camel.evenBusRouteTo", "mock:queue");
        return new EventBusRouteBuilder();
    }

    @Test
    public void testEventBusRoute() throws Exception {
        log.info("Testing event route builder");
        MockEndpoint mockEndpoint = getMockEndpoint("mock:queue");
        // not going to use the enpoint to send to bus going to send direct to bus to test from
        EventBus bus = context().getRegistry().lookupByNameAndType("eventBus", EventBus.class);
        assertNotNull(bus);

        mockEndpoint.expectedMessageCount(1);

        bus.post(new ControlEvent());

        assertMockEndpointsSatisfied(1, TimeUnit.SECONDS);

    }

}