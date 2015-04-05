package com.margic.pihex.camel.route;

import com.margic.pihex.model.ServoConfig;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
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
                .routeId("handleStartupEventRoute")
                .log(LoggingLevel.INFO, "Starting loadServoConfigRoute")
                .to("controlbus:route?routeId=loadServoConfig&action=start");

        from("file:{{config:com.margic.pihex.servo.conf}}?noop=true&charset=UTF-8&include=.*.conf")
                .routeId("loadServoConfig")
                .autoStartup(false)
                .convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Jackson, ServoConfig.class)
                .to(getLoadServoConfigToUri())
                .to("bean:controller?method=handleUpdateServoEvent")
                .choice()
                    .when(exchangeProperty("CamelBatchComplete").isEqualTo(true))
                    .log(LoggingLevel.INFO, "Loaded all servo conf files stopping loader")
                    .process(new StopProcessor())
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

    class StopProcessor implements Processor{
        Thread stop;

        @Override
        public void process(final Exchange exchange) throws Exception {
            // stop this route using a thread that will stop
            // this route gracefully while we are still running
            if (stop == null) {
                stop = new Thread() {
                    @Override
                    public void run() {
                        try {
                            exchange.getContext().stopRoute("loadServoConfig");
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                };
            }

            // start the thread that stops this route
            stop.start();
        }
    }
}
