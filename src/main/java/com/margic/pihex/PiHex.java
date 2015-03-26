package com.margic.pihex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by paulcrofts on 3/15/15.
 *
 * Adding just rough support to start from command line
 * main start up will be via apache commons daemon Jsvc
 * so this helps just to provide a way to start daemon from
 * command until I finish that part.
 * Gives me a way to type exit to shutdown app nicely
 *
 * Don't forget runtime prop   -Dusemock=true when running
 * to run with mock device and avoid native libraries
 */
public class PiHex {
    private static final Logger LOGGER = LoggerFactory.getLogger(PiHex.class);

    private static boolean running;

    public static void main(String... args) {
        LOGGER.info("Starting PiHex application");

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                PiHexDaemon daemon = new PiHexDaemon();
                daemon = new PiHexDaemon();
                daemon.init(new String[]{""});
                daemon.start();
                while (running) {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String command = reader.readLine();
                        if(command.equalsIgnoreCase("exit")) running = false;
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                    } catch (IOException ioe) {
                        LOGGER.error("Error reading input", ioe);
                    }
                }
                daemon.stop();
                daemon.destroy();
            }
        });

        mainThread.start();
    }
}
