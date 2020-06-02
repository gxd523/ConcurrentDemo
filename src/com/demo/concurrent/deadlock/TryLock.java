package com.demo.concurrent.deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLock {
    private static void deadLock(Lock lock1, Lock lock2, int time) throws InterruptedException {
        for (; ; ) {
            if (lock1.tryLock(time, TimeUnit.MILLISECONDS)) {
                Thread.sleep(new Random().nextInt(999));
                System.out.println(String.format(
                        "%s持有锁%s, 请求锁%s",
                        Thread.currentThread().getName(),
                        lock1.hashCode() % 100,
                        lock2.hashCode() % 100
                ));
                if (lock2.tryLock(time, TimeUnit.MILLISECONDS)) {
                    System.out.println(String.format("%s成功拿到锁%s, 成功拿到2把锁!", Thread.currentThread().getName(), lock2.hashCode() % 100));
                    lock2.unlock();
                    lock1.unlock();
                    break;
                } else {
                    System.out.println(String.format("%s获取锁%s失败, 已重试.", Thread.currentThread().getName(), lock2.hashCode() % 100));
                    lock1.unlock();
                    Thread.sleep(new Random().nextInt(999));
                }
            } else {
                System.out.println(String.format("%s获取锁%s失败, 已重试.", Thread.currentThread().getName(), lock1.hashCode() % 100));
            }
        }
    }

    public static void main(String[] args) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        startThread("Thread-A", lock1, lock2, 800);
        startThread("Thread-B", lock2, lock1, 3000);
    }

    private static void startThread(String threadName, Lock l1, Lock l2, int time) {
        new Thread(() -> {
            try {
                deadLock(l1, l2, time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, threadName).start();
    }
}
