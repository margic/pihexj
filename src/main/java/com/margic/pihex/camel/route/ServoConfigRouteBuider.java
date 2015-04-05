package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.camel.converter.ServoCalibrationTypeConverter;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoConfigRouteBuider extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        getContext().getTypeConverterRegistry().addTypeConverter(ServoConfig.class, Servo.class, new ServoCalibrationTypeConverter());
        restConfiguration().component("jetty").host("{{config:com.margic.pihex.api.address}}").port("{{config:com.margic.pihex.api.port}}").bindingMode(RestBindingMode.auto);

        rest("/servoCalibration/")
                .get("/{channel}")
                .outType(ServoConfig.class)
                .to("direct:getServoCalibration")
                .put("/{channel}")
                .consumes("application/json")
                .type(ServoConfig.class)
                .to("direct:putServoCalibration");

        from("direct:getServoCalibration")
                .routeId("getServoCalibration")
                .setBody(header("channel"))
                .to("bean:controller?method=getServo")
                .convertBodyTo(ServoConfig.class);

        // writes servo calibration to file one per servo for now. will consolidate later
        from("direct:putServoCalibration")
                .routeId("putServoCalibration")
                .marshal().json(JsonLibrary.Jackson)
                .to("file://{{config:com.margic.pihex.servo.conf}}?fileName=servo-${in.header.channel}.conf");
    }

}
