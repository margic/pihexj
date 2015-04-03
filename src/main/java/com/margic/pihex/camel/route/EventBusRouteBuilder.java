package com.margic.pihex.camel.route;

import org.apache.camel.builder.RouteBuilder;

/**
 * The eventbus route builder listens to events arriving on the event bus and routes
 * them to the correctly handler.
 * The the controller bean will invoke the correct method depending on the body of the message
 * using camel bean binding
 * Created by paulcrofts on 3/26/15.
 */
public class EventBusRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("guava-eventbus:{{config:com.margic.pihex.camel.eventBusName}}?listenerInterface=com.margic.pihex.camel.route.EventBusEvents")
                .routeId("eventBusRoute")
                .to("bean:controller");
    }
}

