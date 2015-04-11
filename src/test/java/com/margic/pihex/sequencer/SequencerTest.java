package com.margic.pihex.sequencer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by paulcrofts on 4/9/15.
 */
public class SequencerTest {

    @Test
    public void testSequencer() throws Exception{
        Sequencer sequencer = new Sequencer();
        sequencer.start();
        Assert.assertTrue(true);
    }

}
