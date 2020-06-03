package com.demo.concurrent.interview;

public class PrintOddEvenTask implements Runnable {
    private int num;

    @Override
    public void run() {
        while (num < 100) {
            synchronized (this) {
                System.out.printf("%s...%s\n", Thread.currentThread().getName(), num++);
                notify();
                if (num < 100) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Runnable task = new PrintOddEvenTask();
        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }
}
