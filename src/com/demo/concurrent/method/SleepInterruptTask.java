package com.demo.concurrent.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SleepInterruptTask implements Runnable {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        Runnable task = new SleepInterruptTask();
        Thread thread = new Thread(task);
        thread.start();
        Thread.sleep(6500);
        thread.interrupt();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(dateFormat.format(new Date()));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
