package com.margic.pihex.camel;

import com.google.common.eventbus.Subscribe;
import com.margic.pihex.model.ServoSetting;

/**
 * Created by paulcrofts on 3/26/15.
 *
 * This interface is used to provide the types of events that will
 * be processed by camel from the event bus.
 */
public interface EventBusEvents {

    @Subscribe
    public void servoSettingEvent(ServoSetting setting);

}
