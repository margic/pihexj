package com.margic.camel.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.margic.camel.CustomCamelContextTestSupport;
import com.margic.pihex.camel.route.EventBusRouteBuilder;
import com.margic.pihex.camel.route.ServoConfigRouteBuilder;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.AvailablePortFinder;
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
public class ServoConfigPutRouteBuilderTest extends CustomCamelContextTestSupport {
    private int port;

    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {
        log.info(System.getProperties().toString());
        port = AvailablePortFinder.getNextAvailable(8181);
        setConfigurationProperty("com.margic.pihex.api.port", Integer.toString(port));
        setConfigurationProperty("com.margic.pihex.servo.conf", "${sys:user.dir}/target/test-classes/confwritetest/conf/");
        return new RouteBuilder[]{new ServoConfigRouteBuilder(), new EventBusRouteBuilder()};
    }

    @Test
    public void testServoConfigUpdate() throws Exception {

        ServoConfig config = new ServoConfig();

        ObjectMapper mapper = new ObjectMapper();
        String testJson = mapper.writeValueAsString(config);
        log.info("Testing update servo config with {}", testJson);

        int responseStatus = getExecutor().execute(Request.Put("http://localhost:" + port + "/servoconfig/0")
                .addHeader("Content-Type", "application/json")
                .body(new StringEntity(testJson))
                .socketTimeout(0))
                .returnResponse()
                .getStatusLine()
                .getStatusCode();

        log.info("Response status {}", responseStatus);
        assertEquals(204, responseStatus);

        // putting the value should have written an updated config file in conf folder
        String filePath = this.config.getString("com.margic.pihex.servo.conf") + "servo-" + config.getChannel() + ".conf";
        File confFile = new File(filePath);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(confFile))){
            String savedJson = bufferedReader.readLine();
            log.info("JSON written to conf file: {}", savedJson);
            JSONAssert.assertEquals(testJson, savedJson, true);
        }
    }



}
