package com.demo.concurrent.jmm;

import java.util.concurrent.CountDownLatch;

/**
 * 重排序
 */
public class OutOfOrderExecution {
    private static volatile int x, y;
    private static int a, b;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; ; i++) {
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            CountDownLatch countDownLatch = new CountDownLatch(3);

            Thread threadA = new Thread(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a = 1;
                x = b;
            });

            Thread threadB = new Thread(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                b = 1;
                y = a;
            });
            threadA.start();
            threadB.start();

            countDownLatch.countDown();

            threadA.join();
            threadB.join();

            if (!(x == 0 && y == 1) && !(y == 0 && x == 1)) {// 打印异常情况
                System.out.printf("x = %s, y = %s , i = %s%n", x, y, i);
            }
        }
    }
}
