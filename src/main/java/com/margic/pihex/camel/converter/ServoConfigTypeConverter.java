package com.margic.pihex.camel.converter;

import com.margic.pihex.api.Servo;
import com.margic.pihex.model.ServoConfig;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoConfigTypeConverter extends TypeConverterSupport {

    private static final Logger log = LoggerFactory.getLogger(ServoConfigTypeConverter.class);

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        log.debug("Extracting servo config from servo using converter object {}", value);

        if(value instanceof Servo && type.equals(ServoConfig.class)){
            Servo servo = (Servo)value;
            return (T) servo.getServoConfig();
        }
        return null;
    }
}
