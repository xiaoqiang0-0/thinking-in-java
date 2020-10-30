package com.xiaoqiang.cp.lock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

public class CLHLockTest {

    static int index = 0;

    @Test
    public void testLock() throws InterruptedException {
        Lock lock = new CLHLock();
        try {
            lock.lock();
            for (int j = 0; j < 1000; j++) {
                int a = index + 1;
                Thread.yield();
                index = a;
            }
        } finally {
            lock.unlock();
        }
        assertEquals(1000, index);
    }

}