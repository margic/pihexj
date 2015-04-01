package com.margic.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by paulcrofts on 3/30/15.
 */
public class ConfigurationPropertiesFunctionTest extends PiHexCamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        Configuration config = injector.getInstance(Configuration.class);
        config.setProperty("testProperty", "testValue");

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:test")
                        .setBody(simple("{{config:testProperty}}"))
                        .to("mock:mock");
            }
        };
    }
    /**
     * test the properties place holder by setting a value in the config and testing
     * we can use a placeholder in a camel context
     */
    @Test
    public void testPlaceholder() throws Exception{
        MockEndpoint mock = getMockEndpoint("mock:mock");
        template.send("direct:test", createExchangeWithBody(""));
        mock.expectedMessageCount(1);

        Exchange exchange = mock.getExchanges().get(0);
        mock.expectedMessageCount(1);
        assertMockEndpointsSatisfied(1, TimeUnit.SECONDS);

        assertEquals("testValue", exchange.getIn().getBody(String.class));
    }
}
