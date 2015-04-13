package com.margic.pihex.camel.route;

import com.margic.pihex.model.ServoUpdate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by paulcrofts on 4/6/15.
 */
public class ServoUpdateRouteBuilder extends RouteBuilder {

    private static final String DEFAULT_POST_UPDATE_TO_URI = "guava-eventbus:eventBus";

    private String postUpdateToUri;
    /**
     * Set the destination uri for the posting the updates
     * @param uri
     */
    public void setPostUpdateToUri(String uri){
        this.postUpdateToUri = uri;
    }

    public String getPostUpdateToUri(){
        if(postUpdateToUri == null){
            return DEFAULT_POST_UPDATE_TO_URI;
        }else{
            return postUpdateToUri;
        }
    }

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host("{{config:com.margic.pihex.api.address}}").port("{{config:com.margic.pihex.api.port}}").bindingMode(RestBindingMode.auto);

        // receive the post and create an queue an event on the bus
        rest("/servoupdate/")
                .post("/").id("restPostServoUpdate")
                .consumes("application/json")
                .type(ServoUpdate.class)
                .to("direct:postServoUpdate")
                .get("/flush").id("flusheServoUpdates")
                .consumes("application/json")
                .to("direct:getFlushServoUpdates");

        from("direct:getFlushServoUpdates")
                .routeId("getFlushServoUpdates")
                .setBody(simple("ref:flushEvent"))
                .to("bean:controller");

        from("direct:postServoUpdate")
                .routeId("postServoUpdate")
                .to("bean:controller")
                .to(getPostUpdateToUri());
    }
}
