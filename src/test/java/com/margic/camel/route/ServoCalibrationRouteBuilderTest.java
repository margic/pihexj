package com.margic.camel.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.ServoConfigRouteBuider;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
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
public class ServoCalibrationRouteBuilderTest extends CustomCamelContextTestSupport {
    private int port;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        log.info(System.getProperties().toString());
        port = AvailablePortFinder.getNextAvailable(8080);
        setConfigurationProperty("com.margic.pihex.api.port", Integer.toString(port));
        return new ServoConfigRouteBuider();
    }

    @Test
    public void testServoCalibrationUpdate() throws Exception {

        ServoConfig calibration = new ServoConfig();

        ObjectMapper mapper = new ObjectMapper();
        String testJson = mapper.writeValueAsString(calibration);
        log.info("Testing update servo calibration with {}", testJson);

        HttpResponse response = Request.Put("http://localhost:" + port + "/servoCalibration/0")
                .addHeader("Content-Type", "application/json")
                .body(new StringEntity(testJson))
                .execute()
                .returnResponse();


        log.info("Response status {}", response.getStatusLine().toString());
        assertEquals(200, response.getStatusLine().getStatusCode());

        // putting the value should have written an updated config file in conf folder
        String filePath = config.getString("com.margic.pihex.servo.conf") + "servo-" + calibration.getChannel() + ".conf";
        File confFile = new File(filePath);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(confFile))){
            String savedJson = bufferedReader.readLine();
            log.info("JSON written to conf file: {}", savedJson);
            JSONAssert.assertEquals(testJson, savedJson, false);
        }
    }

    @Test
    public void testServoCalibrationGet() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:mock");

        Content content = Request.Get("http://localhost:" + port + "/servoCalibration/0")
                .addHeader("Accept", "application/json")
                .execute()
                .returnContent();

        log.info(content.asString());
        JSONAssert.assertEquals("{\"channel\":0,\"range\":180,\"center\":0,\"lowLimit\":-90,\"highLimit\":90}", content.asString(), false);
    }

}
