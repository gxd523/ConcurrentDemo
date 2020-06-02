package com.demo.concurrent.deadlock;

import java.util.concurrent.TimeUnit;

/**
 * 简单的多线程连环死锁
 */
public class ChainLock {
    private static void deadLock(Object l1, Object l2) throws InterruptedException {
        synchronized (l1) {
            TimeUnit.SECONDS.sleep(1);
            printInfo(l1, l2);
            synchronized (l2) {
                System.out.println("...");
            }
        }
    }

    public static void main(String[] args) {
        Object[] locks = new Object[5];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }

        for (int i = 0; i < locks.length; i++) {
            Object l1 = locks[i];
            Object l2 = locks[(i + 1) % locks.length];

            new Thread(() -> {
                try {
                    deadLock(l1, l2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.format("Thread-%s", i)).start();
        }
    }

    private static void printInfo(Object l1, Object l2) {
        System.out.printf(
                "%s拿到锁%s,等待锁%s\n",
                Thread.currentThread().getName(),
                l1.hashCode() % 100, l2.hashCode() % 100
        );
    }
}
