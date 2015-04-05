package com.margic.pihex.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by paulcrofts on 4/4/15.
 */
public class CalibrationFileAggregation implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        String calibration = newExchange.getIn().getBody(String.class);

        return null;
    }
}
