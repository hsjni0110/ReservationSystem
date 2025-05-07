package com.reservation.reservationsystem.thread;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VirtualThreadTest {

    @Test
    public void testIoBoundVirtualThread() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread ioBoundThread = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(300);
                Thread.sleep(300);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ioBoundThread.join();

        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) >= 900, "The ioBound method should take around 900 ms (300 ms * 3 calls).");
    }

}