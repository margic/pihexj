package com.margic.adafruitpwm;

import org.apache.commons.io.HexDump;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Bit conversion utilities for converting ints to bytes for
 * read write operations
 * Created by paulcrofts on 3/19/15.
 */
public class ByteUtils {

    public static final int LOW_BYTE = 0;
    public static final int HIGH_BYTE = 1;


    public static int getInt(byte low, byte high) {
        int value = (high << 8) | (low & 0xFF);
        return value;
    }

    /**
     * utlitiy method that takes an int value and returns
     * the low and high bytes as and array
     *
     * @param value
     * @return
     */
    public static byte[] get2ByteInt(int value) {
        byte[] bytes = new byte[2];
        bytes[LOW_BYTE] = (byte) (value & 0xFF);
        bytes[HIGH_BYTE] = (byte) (value >> 8);
        return bytes;
    }

    /**
     * Utility method for dumping a byte array to hex string for logging
     *
     * @param bytes
     * @return
     */
    public static String dumpHex(byte[] bytes) {
        String hex;
        if (bytes == null || bytes.length == 0) {
            return "No bytes to dump";
        }
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            HexDump.dump(bytes, 0, out, 0);
            hex = new String(out.toByteArray(), "UTF-8");
        } catch (IOException ioe) {
            // not doing anything with the exception the dump is for info in log anyway just return the message if something goes wrong
            return ioe.getMessage();
        }
        return hex;
    }
}
