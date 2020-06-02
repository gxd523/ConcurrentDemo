package com.demo.concurrent.deadlock;

public class DeadLockTask {
    private static void deadLock(Object lock1, Object lock2) {
        synchronized (lock1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printInfo(lock1, lock2);
            synchronized (lock2) {
                System.out.println(String.format("%s成功拿到2把锁", Thread.currentThread().getName()));
            }
        }
    }

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        new Thread(() -> deadLock(lock1, lock2), "Thread-A").start();
        new Thread(() -> deadLock(lock2, lock1), "Thread-B").start();
    }

    private static void printInfo(Object lock1, Object lock2) {
        System.out.println(String.format(
                "%s持有锁%s, 请求锁%s",
                Thread.currentThread().getName(),
                lock1.hashCode() % 100,
                lock2.hashCode() % 100
        ));
    }
}
