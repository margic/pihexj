package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.camel.converter.ServoConfigTypeConverter;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoConfigRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        getContext().getTypeConverterRegistry().addTypeConverter(ServoConfig.class, Servo.class, new ServoConfigTypeConverter());
        restConfiguration().component("jetty").host("{{config:com.margic.pihex.api.address}}").port("{{config:com.margic.pihex.api.port}}").bindingMode(RestBindingMode.auto);

        rest("/servoconfig/")
                .get("/{channel}").id("restGetServoConfig")
                .outType(ServoConfig.class)
                .to("direct:getServoConfig")
                .put("/{channel}").id("restPutServoConfig")
                .consumes("application/json")
                .type(ServoConfig.class)
                .to("direct:putServoConfig");

        from("direct:getServoConfig")
                .routeId("getServoConfig")
                .setBody(header("channel"))
                .to("bean:controller")
                .convertBodyTo(ServoConfig.class);

        // writes servo calibration to file one per servo for now. will consolidate later
        from("direct:putServoConfig")
                .routeId("putServoConfig")
                .multicast()
                    .to("direct:updateRunningConfig")
                    .to("direct:writeConfigToFile")
                .end()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("204"));

        from("direct:updateRunningConfig")
                .routeId("updateRunningConfig")
                .to("bean:controller")
                .to("guava-eventbus:eventBus");

        from("direct:writeConfigToFile")
                .id("writeConfigToFile")
                .marshal().json(JsonLibrary.Jackson)
                .to("file://{{config:com.margic.pihex.servo.conf}}?fileName=servo-${in.header.channel}.conf");
    }

}
