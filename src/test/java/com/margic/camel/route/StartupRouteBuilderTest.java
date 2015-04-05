package com.margic.camel.route;

import com.google.common.eventbus.EventBus;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import com.margic.pihex.camel.route.StartupRouteBuilder;
import com.margic.pihex.event.StartupEvent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class StartupRouteBuilderTest extends CustomCamelContextTestSupport {

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        RouteBuilder[] routes = new RouteBuilder[2];
        StartupRouteBuilder startupRoute = new StartupRouteBuilder();
        startupRoute.setLoadServoConfigToUri("mock:startup");
        routes[0] = startupRoute;
        routes[1] = new EventBusRouteBuilder();
        return routes;
    }

    @Test
    public void startupTest() throws Exception{

        MockEndpoint mock = getMockEndpoint("mock:startup");

        mock.expectedMessageCount(2);
        // get the event bus and trigger start event
        EventBus eventBus = context().getRegistry().lookupByNameAndType("eventBus", EventBus.class);

        eventBus.post(new StartupEvent());
        assertMockEndpointsSatisfied(10, TimeUnit.SECONDS);

    }
}
