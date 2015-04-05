package com.margic.pihex.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class StartupRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct-vm:handleStartupEvent")
                .log(LoggingLevel.INFO, "Handling startup event")
                .pollEnrich("file:{{config:com.margic.pihex.servo.conf}}?noop=true&charset=UTF-8&include=.*.conf", new CalibrationFileAggregation())
                .to("mock:startupMock");

    }

}
