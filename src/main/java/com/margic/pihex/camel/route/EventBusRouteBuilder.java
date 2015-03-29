package com.margic.pihex.camel.route;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by paulcrofts on 3/26/15.
 */
public class EventBusRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("guava-eventbus:{{com.margic.pihex.camel.eventBusName}}?listenerInterface=com.margic.pihex.camel.route.EventBusEvents").to("seda:queue");
    }
}

