package com.margic.camel.route;

import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.ServoUpdateRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by paulcrofts on 4/6/15.
 */
public class ServoUpdateRouteBuilderTest extends CustomCamelContextTestSupport {
    private int port;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        port = AvailablePortFinder.getNextAvailable(8181);
        ServoUpdateRouteBuilder rb = new ServoUpdateRouteBuilder();
        rb.setPostUpdateToUri("mock:postUpdate");
        return rb;
    }

    @Override
    public boolean isCreateCamelContextPerClass() {
        return true;
    }

    @Test
    public void testPostServoUpdate() throws Exception{
        MockEndpoint mock = getMockEndpoint("mock:postUpdate");

        mock.expectedMessageCount(1);


        String testJson = "{\"channel\":0,\"angle\":10}";
        int responseStatus = 0;

        try {
            responseStatus = getExecutor().execute(Request.Post("http://localhost:" + port + "/servoupdate")
                    .addHeader("Content-Type", "application/json")
                    .body(new StringEntity(testJson))
                    .socketTimeout(0))
                    .returnResponse()
                    .getStatusLine()
                    .getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Response status {}", responseStatus);
        assertEquals(200, responseStatus);
        assertMockEndpointsSatisfied(1, TimeUnit.SECONDS);
    }
}
