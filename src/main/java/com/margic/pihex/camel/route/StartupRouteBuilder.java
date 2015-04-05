package com.margic.pihex.camel.route;

import com.margic.pihex.model.ServoConfig;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class StartupRouteBuilder extends RouteBuilder {

    private static final String DEFAULT_LOAD_SERVO_CONFIG_TO_URI = "bean:controller";
    private String loadServoConfigToUri;

    @Override
    public void configure() throws Exception {
        from("direct-vm:handleStartupEvent")
                .log(LoggingLevel.INFO, "Starting loadServoConfigRoute")
                .to("controlbus:route?routeId=loadServoConfig&action=start");

        from("file:{{config:com.margic.pihex.servo.conf}}?noop=true&charset=UTF-8&include=.*.conf")
                .routeId("loadServoConfig")
                .autoStartup(false)
                .convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Jackson, ServoConfig.class)
                .to(getLoadServoConfigToUri())
                .choice()
                    .when(exchangeProperty("CamelBatchComplete").isEqualTo(true))
                    .log(LoggingLevel.INFO, "Loaded all servo conf files stopping loader")
                    .stop()
                .endChoice();
    }

    /**
     * Allows overriding of the endpoint for the loadServoConfig route
     * for testing. call this with a mock endpoint to test configs
     * are being loaded correctly
     * @param uri
     */
    public void setLoadServoConfigToUri(String uri){
        this.loadServoConfigToUri = uri;
    }

    public String getLoadServoConfigToUri(){
        if(loadServoConfigToUri == null){
            return DEFAULT_LOAD_SERVO_CONFIG_TO_URI;
        }else{
            return loadServoConfigToUri;
        }
    }
}
