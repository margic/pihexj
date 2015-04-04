package com.margic.pihex.camel.converter;

import com.margic.pihex.api.Servo;
import com.margic.pihex.model.ServoCalibration;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 4/2/15.
 */
public class ServoCalibrationTypeConverter extends TypeConverterSupport {

    private static final Logger log = LoggerFactory.getLogger(ServoCalibrationTypeConverter.class);

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        log.debug("Extracting servo calibration from servo using converter");

        if(value instanceof Servo && type.equals(ServoCalibration.class)){
            Servo servo = (Servo)value;
            ServoCalibration servoCalibration = new ServoCalibration();
            servoCalibration.setChannel(servo.getChannel());
            servoCalibration.setCenter(servo.getCenter());
            servoCalibration.setHighLimit(servo.getHighLimit());
            servoCalibration.setLowLimit(servo.getLowLimit());
            servoCalibration.setRange(servo.getRange());
            return (T) servoCalibration;
        }
        return null;
    }
}
