package com.margic.pihex.camel.route;

import com.margic.pihex.api.Servo;
import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.event.ServoUpdateEvent;
import com.margic.pihex.event.StartupEvent;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * The eventbus route builder listens to events arriving on the event bus and routes
 * them to the the appropriate handler.
 * Created by paulcrofts on 3/26/15.
 */
public class EventBusRouteBuilder extends RouteBuilder {

    private static final String DEFAULT_UPDATE_SERVO_TO_URI = "bean:controller";

    private String updateServoToUri;

    public void setUpdateServoToUri(String uri){
        this.updateServoToUri = uri;
    }

    public String getUpdateServoToUri(){
        if(updateServoToUri == null){
            return DEFAULT_UPDATE_SERVO_TO_URI;
        }else {
            return updateServoToUri;
        }
    }


    @Override
    public void configure() throws Exception {
        from("guava-eventbus:{{config:com.margic.pihex.camel.eventBusName}}?listenerInterface=com.margic.pihex.camel.route.EventBusEvents")
                .routeId("eventBusRoute")
                .choice()
                    .when(body().isInstanceOf(ControlEvent.class))
                        .to("bean:controller")
                    .when(body().isInstanceOf(StartupEvent.class))
                        .to("direct:handleStartupEvent")
                    .when(body().isInstanceOf(ServoUpdateEvent.class))
                        .to("seda:updateServo")
                .endChoice();

        from("seda:updateServo")
                .routeId("updateServo")
                .to(getUpdateServoToUri());
    }
}

