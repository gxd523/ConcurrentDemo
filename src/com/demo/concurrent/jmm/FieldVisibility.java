package com.demo.concurrent.jmm;

import java.util.concurrent.CountDownLatch;

public class FieldVisibility {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    int a = 1;
    int b = 2;

    public static void main(String[] args) {
        while (true) {
            FieldVisibility fieldVisibility = new FieldVisibility();
            new Thread(() -> {
                fieldVisibility.countDownLatch.countDown();
                try {
                    fieldVisibility.countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fieldVisibility.write();
            }).start();

            new Thread(() -> {
                fieldVisibility.countDownLatch.countDown();
                try {
                    fieldVisibility.countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fieldVisibility.read();
            }).start();
        }
    }

    private void write() {
        a = 3;
        b = 3;
    }

    private void read() {
        System.out.println(String.format("b = %s, a = %s", b, a));
    }
}
