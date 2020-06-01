package com.demo.concurrent.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadMXBeanDetection {
    private static void deadLock(Object lock1, Object lock2) {
        synchronized (lock1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format(
                    "%s持有锁%s, 请求锁%s",
                    Thread.currentThread().getName(),
                    lock1.hashCode() % 100,
                    lock2.hashCode() % 100
            ));
            synchronized (lock2) {
                System.out.println(String.format("%s成功拿到2把锁", Thread.currentThread().getName()));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        new Thread(() -> deadLock(lock1, lock2), "Thread-A").start();
        new Thread(() -> deadLock(lock2, lock1), "Thread-B").start();

        Thread.sleep(1000);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            for (long deadlockedThread : deadlockedThreads) {
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThread);
                System.out.println(String.format("%s发生死锁", threadInfo.getThreadName()));
            }
        }
    }
}
