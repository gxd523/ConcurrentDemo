package com.demo.concurrent.interview;

public class PrintOddEvenTask implements Runnable {
    private int num;

    public static void main(String[] args) {
        Runnable task = new PrintOddEvenTask();
        new Thread(task, "偶数").start();
        new Thread(task, "奇数").start();
    }

    @Override
    public void run() {
        while (num < 100) {
            synchronized (this) {
                System.out.println(String.format("%s...%s", Thread.currentThread().getName(), num++));
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
}
