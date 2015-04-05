package com.margic.model;

import com.margic.pihex.model.ServoConfig;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by paulcrofts on 4/5/15.
 */
public class ServoConfigTest {
    
    @Test
    public void testEquals() throws Exception{
        ServoConfig config1 = new ServoConfig.Builder()
                .center(0)
                .build();
        
        ServoConfig config2 = new ServoConfig.Builder()
                .center(0)
                .build();

        assertEquals(config1, config2);
        assertTrue(config1.equals(config2));
    }

    @Test
    public void testNotEquals() throws Exception{
        ServoConfig config1 = new ServoConfig.Builder()
                .center(1)
                .build();

        ServoConfig config2 = new ServoConfig.Builder()
                .center(0)
                .build();

        assertNotEquals(config1, config2);
        assertFalse(config1.equals(config2));
    }
            
}
