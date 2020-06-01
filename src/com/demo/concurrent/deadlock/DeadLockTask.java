package com.demo.concurrent.deadlock;

public class DeadLockTask {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println("线程A拿到锁A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程A请求锁B");
                synchronized (lock2) {
                    System.out.println("aaaa");
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (lock2) {
                System.out.println("线程B拿到锁B");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程B请求锁A");
                synchronized (lock1) {
                    System.out.println("bbbb");
                }
            }
        }).start();
    }
}
