package com.demo.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintOddEvenTask implements Runnable {
    private int num;

    @Override
    public void run() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        while (num < 100) {
            lock.lock();
            System.out.printf("%s...%s\n", Thread.currentThread().getName(), num++);
            condition.signal();
            if (num < 100) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Runnable task = new com.demo.concurrent.interview.PrintOddEvenTask();
        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }
}