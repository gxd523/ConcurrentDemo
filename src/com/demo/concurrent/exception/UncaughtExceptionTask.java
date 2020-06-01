package com.demo.concurrent.exception;

import java.util.concurrent.TimeUnit;

public class UncaughtExceptionTask implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler("MyCatcher"));

        new Thread(new UncaughtExceptionTask(), "Thread-1").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(new UncaughtExceptionTask(), "Thread-2").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(new UncaughtExceptionTask(), "Thread-3").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(new UncaughtExceptionTask(), "Thread-4").start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结束");
    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
