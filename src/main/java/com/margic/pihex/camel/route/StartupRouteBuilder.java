package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.Exchange;
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
        from("direct:handleStartupEvent")
                .routeId("handleStartupEventRoute")
                .log(LoggingLevel.INFO, "Starting loadServoConfigRoute")
                .to("controlbus:route?routeId=loadServoConfig&action=start");

        from("file:{{config:com.margic.pihex.servo.conf}}?noop=true&charset=UTF-8&include=.*.conf")
                .routeId("loadServoConfig")
                .autoStartup(false)
                .log(LoggingLevel.INFO, "loading config file ${in.header.CamelFileName}")
                .unmarshal().json(JsonLibrary.Jackson, ServoConfig.class)
                .setHeader("startAngle").ognl("request.body.startAngle")
                .to(getLoadServoConfigToUri())
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // creating a new servo update even to set the initial position of the servo to the start position
                        Servo servo = exchange.getIn().getBody(Servo.class);
                        exchange.getIn().setBody(new ServoUpdateEvent(servo, exchange.getIn().getHeader("startAngle", int.class)));
                    }
                })
                .to("guava-eventbus:eventBus")
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
                        } catch (Exception e) {}
                    }
                };
            }
            stop.start();
        }
    }
}
