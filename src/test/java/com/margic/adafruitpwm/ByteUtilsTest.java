package com.margic.adafruitpwm;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by paulcrofts on 3/19/15.
 */
public class ByteUtilsTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ByteUtilsTest.class);


    @Test
    public void testGetInt() {
        assertEquals(0, ByteUtils.getInt((byte) 0x00, (byte) 0x00));
        assertEquals(4095, ByteUtils.getInt((byte) 0xFF, (byte) 0x0F));
        assertEquals(255, ByteUtils.getInt((byte) 0xFF, (byte) 0x00));
        assertEquals(256, ByteUtils.getInt((byte) 0x00, (byte) 0x01));
        assertEquals(1, ByteUtils.getInt((byte) 0x01, (byte) 0x00));

    }

    @Test
    public void testGet2ByteInt() {
        assert2ByteInt(ByteUtils.get2ByteInt(255), (byte) 0xFF, (byte) 0x00);
        assert2ByteInt(ByteUtils.get2ByteInt(256), (byte) 0x00, (byte) 0x01);
        assert2ByteInt(ByteUtils.get2ByteInt(4095), (byte) 0xFF, (byte) 0x0F);
        assert2ByteInt(ByteUtils.get2ByteInt(0), (byte) 0x00, (byte) 0x00);
        assert2ByteInt(ByteUtils.get2ByteInt(1), (byte) 0x01, (byte) 0x00);
    }


    private void assert2ByteInt(byte[] testValue, byte lowExpected, byte highExpected) {
        LOGGER.info("Bytes for value {}:{}", testValue, ByteUtils.dumpHex(testValue));
        assertEquals(lowExpected, testValue[ByteUtils.LOW_BYTE]);
        assertEquals(highExpected, testValue[ByteUtils.HIGH_BYTE]);
    }
}