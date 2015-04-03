package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.camel.converter.ServoCalibrationTypeConverter;
import com.margic.pihex.model.ServoCalibration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoCalibrationRouteBuider extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        getContext().getTypeConverterRegistry().addTypeConverter(ServoCalibration.class, Servo.class, new ServoCalibrationTypeConverter());
        restConfiguration().component("jetty").host("0.0.0.0").port("{{config:com.margic.pihex.api.port}}").bindingMode(RestBindingMode.auto);

        rest("/servoCalibration/")
                .get("/{channel}")
                .outType(ServoCalibration.class)
                .to("direct:getServoCalibration")
                .put("/{channel}")
                .consumes("application/json")
                .type(ServoCalibration.class)
                .to("guava-eventbus:{{config:com.margic.pihex.camel.eventBusName}}");


        from("direct:getServoCalibration")
                .routeId("getServoCalibration")
                .setBody(header("channel"))
                .to("bean:body?method=getServo")
                .convertBodyTo(ServoCalibration.class);
    }

}
