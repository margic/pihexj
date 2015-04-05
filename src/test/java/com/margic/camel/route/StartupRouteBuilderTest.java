package com.margic.camel.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import com.margic.pihex.camel.route.StartupRouteBuilder;
import com.margic.pihex.event.StartupEvent;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class StartupRouteBuilderTest extends CustomCamelContextTestSupport {

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        setConfigurationProperty("com.margic.pihex.servo.conf", "${sys:user.dir}/target/test-classes/startuptest/conf/");
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

        String servo0json = "{\"channel\":0,\"range\":180,\"center\":5,\"lowLimit\":-90,\"highLimit\":90,\"name\":\"Leg 0 Coxa\"}";

        String servo2json = "{\"channel\":2,\"range\":120,\"center\":-5,\"lowLimit\":-60,\"highLimit\":60,\"name\":\"Leg 0 Tibia\"}";

        ObjectMapper mapper = new ObjectMapper();
        ServoConfig servo0 = mapper.readValue(servo0json, ServoConfig.class);
        ServoConfig servo2 = mapper.readValue(servo2json, ServoConfig.class);

        mock.expectedBodiesReceived(servo0, servo2);

        // get the event bus and trigger start event
        EventBus eventBus = context().getRegistry().lookupByNameAndType("eventBus", EventBus.class);

        // test the loader route is not running before event set.
        assertFalse(context().getRouteStatus("loadServoConfig").isStarted());

        eventBus.post(new StartupEvent());

        assertMockEndpointsSatisfied(10, TimeUnit.SECONDS);
    }
}
