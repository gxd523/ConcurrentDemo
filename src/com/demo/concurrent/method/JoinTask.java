package com.demo.concurrent.method;

import java.util.concurrent.TimeUnit;

public class JoinTask {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();

        Thread thread = new Thread(() -> {
            System.out.printf("%s...开始执行%n", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
//                mainThread.interrupt();// 中断主线程会导致join中断异常
//                TimeUnit.SECONDS.sleep(3);
                System.out.printf("%s...%s\n", mainThread.getName(), mainThread.getState());
            } catch (InterruptedException e) {
                System.out.printf("%s...被中断了(sleep)%n", Thread.currentThread().getName());
            }
            System.out.printf("%s...执行结束%n", Thread.currentThread().getName());
        }, "Thread-A");

        thread.start();

        System.out.printf("%s...开始等待子线程运行完毕%n", Thread.currentThread().getName());
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.printf("%s...被中断了(join)%n", Thread.currentThread().getName());
            thread.interrupt();// 传递中断，不然catch异常后中断标记又还原了
        }

        System.out.println("所有子线程执行完毕..." + thread.getPriority());
    }
}
