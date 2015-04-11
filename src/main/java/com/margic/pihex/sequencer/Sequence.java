package com.margic.pihex.sequencer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulcrofts on 4/9/15.
 * A sequence is a number of tracks that will generate servo update events when
 * played back by the sequencer
 */
public class Sequence {

    private static final Logger log = LoggerFactory.getLogger(Sequence.class);

    private List<Track> tracks;

    public Sequence() {
        tracks = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }
}
