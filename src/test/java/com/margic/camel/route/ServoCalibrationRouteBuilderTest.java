package com.margic.camel.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.ServoCalibrationRouteBuider;
import com.margic.pihex.model.ServoCalibration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoCalibrationRouteBuilderTest extends CustomCamelContextTestSupport {
    private int port;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        port = AvailablePortFinder.getNextAvailable(8080);
        setConfigurationProperty("com.margic.pihex.api.port", Integer.toString(port));
        return new ServoCalibrationRouteBuider();
    }

    @Test
    public void testServoCalibrationUpdate() throws Exception {
        ServoCalibration calibration = new ServoCalibration();

        ObjectMapper mapper = new ObjectMapper();
        String testJson = mapper.writeValueAsString(calibration);
        log.info("Testing update servo calibration with {}", testJson);

        HttpResponse response = Request.Put("http://localhost:" + port + "/servoCalibration/0")
                .addHeader("Content-Type", "application/json")
                .body(new StringEntity(testJson))
                .execute()
                .returnResponse();

        log.info("Response status {}", response.getStatusLine().toString());
    }

    @Test
    public void testServoCalibrationGet() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:mock");

        Content content = Request.Get("http://localhost:" + port + "/servoCalibration/0")
                .addHeader("Accept", "application/json")
                .execute()
                .returnContent();

        log.info(content.asString());
        JSONAssert.assertEquals("{\"channel\":0,\"name\":\"Leg 0 Coxa\",\"range\":180,\"center\":0,\"lowLimit\":-90,\"highLimit\":90}", content.asString(), false);
    }

}
