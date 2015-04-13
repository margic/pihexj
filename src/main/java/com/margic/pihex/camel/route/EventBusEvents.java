package com.margic.pihex.camel.route;

import com.google.common.eventbus.Subscribe;
import com.margic.pihex.event.*;

/**
 * Created by paulcrofts on 3/26/15.
 * <p>
 * This interface is used to provide the types of events that will
 * be processed by camel from the event bus.
 */
public interface EventBusEvents {

    /**
     * Posting a servo object to the bus will send the
     * servo to a seda queue for serial processing of updates
     * to position of servos
     *
     * @param servoUpdateEvent
     */
    @Subscribe
    public void servoUpdateEvent(ServoUpdateEvent servoUpdateEvent);

    /**
     * An event that should cause the robot to move
     *
     * @param moveEvent
     */
    @Subscribe
    public void controlEvent(ControlEvent moveEvent);

    /**
     * Event that represents feedback coming from a sensor
     *
     * @param sensorEvent
     */
    @Subscribe
    public void sensorInterruptEvent(SensorInterruptEvent sensorEvent);

    @Subscribe
    public void startUpEvent(StartupEvent startupEvent);

    @Subscribe
    public void flushServoUpdates(FlushServoUpdateEvent flushEvent);

}
