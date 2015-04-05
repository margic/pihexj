package com.margic.camel.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.ServoConfigRouteBuilder;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoConfigRouteGetBuilderTest extends CustomCamelContextTestSupport {
    private int port;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        log.info(System.getProperties().toString());
        port = AvailablePortFinder.getNextAvailable(8080);
        setConfigurationProperty("com.margic.pihex.api.port", Integer.toString(port));
        setConfigurationProperty("com.margic.pihex.servo.conf", "${sys:user.dir}/target/test-classes/confwritetest/conf/");
        return new ServoConfigRouteBuilder();
    }

    @Test
    public void testServoConfigGet() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:mock");

        String content = Request.Get("http://localhost:" + port + "/servoconfig/0")
                .addHeader("Accept", "application/json")
                .execute()
                .returnContent()
                .asString();


        log.info(content);
        JSONAssert.assertEquals("{\"name\":\"Leg 0 Coxa\",\"channel\":0,\"range\":180,\"center\":0,\"lowLimit\":-90,\"highLimit\":90}", content, true);
    }

}
