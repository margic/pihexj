package com.margic.pihex.sequencer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by paulcrofts on 4/9/15.
 */
public class Sequencer {

    private static final Logger log = LoggerFactory.getLogger(Sequencer.class);

    private ScheduledThreadPoolExecutor executor;

    public Sequencer() {
        this.executor = new ScheduledThreadPoolExecutor(2);


    }


    public void start() throws Exception {
        ScheduledFuture scheduledFuture = executor.scheduleAtFixedRate(new RunSequence(new Sequence()), 0, 100, TimeUnit.MILLISECONDS);
    }


    class RunSequence implements Runnable{

        private Sequence sequence;
        private int currentTick;

        public RunSequence(Sequence sequence){
            this.sequence = sequence;
        }

        @Override
        public void run() {

        }
    }
}
