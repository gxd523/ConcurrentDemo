package com.demo.concurrent.method;

import java.util.concurrent.TimeUnit;

public class JoinTask {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();

        Thread thread1 = new Thread(() -> {
            System.out.println(String.format("%s...开始执行", Thread.currentThread().getName()));
            try {
                TimeUnit.SECONDS.sleep(3);
//                mainThread.interrupt();
                TimeUnit.SECONDS.sleep(3);
                System.out.println(mainThread.getState());
            } catch (InterruptedException e) {
                System.out.println(String.format("%s...被中断了(sleep)", Thread.currentThread().getName()));
            }
            System.out.println(String.format("%s...执行结束", Thread.currentThread().getName()));
        }, "Thread-A");

        thread1.start();

        System.out.println(String.format("%s...开始等待子线程运行完毕", Thread.currentThread().getName()));
        try {
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println(String.format("%s...被中断了(join)", Thread.currentThread().getName()));
            thread1.interrupt();
        }

        System.out.println("所有子线程执行完毕" + thread1.getPriority());

    }
}
