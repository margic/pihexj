package com.margic.pihex.event;

import com.margic.pihex.ServoImpl;
import com.margic.pihex.model.ServoConfig;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;


/**
 * Created by paulcrofts on 4/10/15.
 */
public class ServoUpdateEventTest {

    /**
     * Need to test compare behaves as expected
     * add servos in any order and get them back ordered by
     * channel
     */
    @Test
    public void testCompare(){
        Set<ServoUpdateEvent> set = new TreeSet<>();
        ServoUpdateEvent event1 = new ServoUpdateEvent(new ServoImpl(new ServoConfig.Builder().channel(4).build()), 0);
        ServoUpdateEvent event2 = new ServoUpdateEvent(new ServoImpl(new ServoConfig.Builder().channel(2).build()), 0);
        ServoUpdateEvent event3 = new ServoUpdateEvent(new ServoImpl(new ServoConfig.Builder().channel(1).build()), 0);
        ServoUpdateEvent event4 = new ServoUpdateEvent(new ServoImpl(new ServoConfig.Builder().channel(3).build()), 0);
        
        set.add(event1);
        set.add(event2);
        set.add(event3);
        set.add(event4);

        int count = 1;
        for(ServoUpdateEvent event: set){
            assertEquals(count, event.getServo().getServoConfig().getChannel());
            count++;
        }
        
    }


}
