package com.margic.adafruitpwm.mock;

import com.margic.adafruitpwm.PCA9685Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paulcrofts on 3/18/15.
 */
public class MockPCA9685Device implements PCA9685Device{

    private static Logger LOGGER = LoggerFactory.getLogger(MockPCA9685Device.class);

    private static String NAME = "Mock PCA9685 device";

    private byte[] registers;

    public MockPCA9685Device(){
        // device has 256 registers
        registers = new byte[256];
    }

    @Override
    public String getDeviceName() {
        return NAME;
    }

    @Override
    public void init() {

    }

    @Override
    public void setAllLEDPulse(int[] servoPulseArray) {

    }

    @Override
    public void setLEDPulse(int ledChannel, int pulseLength) {

    }
}
