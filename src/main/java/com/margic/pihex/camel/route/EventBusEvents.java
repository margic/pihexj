package com.margic.pihex.camel.route;

import com.google.common.eventbus.Subscribe;
import com.margic.pihex.event.ControlEvent;
import com.margic.pihex.event.SensorInterruptEvent;
import com.margic.pihex.model.ServoCalibration;
import com.margic.pihex.event.ServoUpdateEvent;

/**
 * Created by paulcrofts on 3/26/15.
 *
 * This interface is used to provide the types of events that will
 * be processed by camel from the event bus.
 */
public interface EventBusEvents {

    /**
     * Low level servo update event causes a servo position to be updated
     * @param servoUpdateEvent
     */
    @Subscribe
    public void servoUpdateEvent(ServoUpdateEvent servoUpdateEvent);

    /**
     * An event that should cause the robot to move
     * @param moveEvent
     */
    @Subscribe
    public void controlEvent(ControlEvent moveEvent);

    /**
     * Event that represents feedback coming from a sensor
     * @param sensorEvent
     */
    @Subscribe
    public void sensorInterruptEvent(SensorInterruptEvent sensorEvent);

    /**
     * Event that represents a request to update a servo's calibration
     */
    @Subscribe
    public void servoCalibrationEvent(ServoCalibration servoCalibration);

}
