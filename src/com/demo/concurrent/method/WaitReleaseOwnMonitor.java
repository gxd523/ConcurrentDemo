package com.demo.concurrent.method;

public class WaitReleaseOwnMonitor {
    private static final Object objA = new Object();
    private static final Object objB = new Object();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (objA) {
                System.out.println(Thread.currentThread().getName() + " 拿到锁A");
                synchronized (objB) {
                    System.out.println(Thread.currentThread().getName() + " 拿到锁B");
                    try {
                        System.out.println(Thread.currentThread().getName() + " 释放锁A");
                        objA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Thread-A");

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (objA) {
                System.out.println(Thread.currentThread().getName() + " 拿到锁A");
                synchronized (objB) {
                    System.out.println(Thread.currentThread().getName() + " 拿到锁B");
                }
            }
        }, "Thread-B");

        threadA.start();
        threadB.start();
    }
}
