package com.margic.pihex.camel.route;

import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.event.StartupEvent;
import org.apache.camel.builder.RouteBuilder;

/**
 * The eventbus route builder listens to events arriving on the event bus and routes
 * them to the the appropriate handler.
 * Created by paulcrofts on 3/26/15.
 */
public class EventBusRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("guava-eventbus:{{config:com.margic.pihex.camel.eventBusName}}?listenerInterface=com.margic.pihex.camel.route.EventBusEvents")
                .routeId("eventBusRoute")
                .choice()
                    .when(body().isInstanceOf(ControlEvent.class))
                        .to("bean:controller?method=handleControlEvent")
                    .when(body().isInstanceOf(StartupEvent.class))
                        .to("direct-vm:handleStartupEvent")
                .endChoice();
    }
}

