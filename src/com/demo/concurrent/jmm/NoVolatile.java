package com.demo.concurrent.jmm;

import java.util.concurrent.atomic.AtomicInteger;

public class NoVolatile implements Runnable {
    static volatile int a;
    static AtomicInteger b = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        NoVolatile task = new NoVolatile();
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(() -> {
            for (int i = 10000; i < 20000; i++) {
                a = i;
                b.incrementAndGet();
            }
        });

        thread1.start();
        Thread.sleep(1);
        thread2.start();

        thread1.join();
        System.out.println("...");
        thread2.join();

        System.out.println(String.format("a = %s", a));
        System.out.println(String.format("b = %s", b.get()));
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a = i;
            b.incrementAndGet();
        }
    }
}
