package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.camel.converter.ServoCalibrationTypeConverter;
import com.margic.pihex.model.ServoCalibration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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
                .to("direct:putServoCalibration");


        from("direct:getServoCalibration")
                .routeId("getServoCalibration")
                .setBody(header("channel"))
                .to("bean:controller?method=getServo")
                .convertBodyTo(ServoCalibration.class);

        // writes servo calibration to file one per servo for now. will consolidate later
        from("direct:putServoCalibration")
                .routeId("putServoCalibration")

                .marshal().json(JsonLibrary.Jackson)
                .to("{{config:com.margic.pihex.servo.calibration.uri}}?fileName=servo-${in.header.channel}.conf");
    }

}