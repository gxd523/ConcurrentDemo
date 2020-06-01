package com.demo.concurrent.interview;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


public class ConcurrentSecurityTask implements Runnable {
    private int index;
    private final boolean[] marked = new boolean[20001];
    private final AtomicInteger realIndex = new AtomicInteger();
    private final AtomicInteger wrongCount = new AtomicInteger();

    volatile CyclicBarrier cyclicBarrier1 = new CyclicBarrier(2);
    volatile CyclicBarrier cyclicBarrier2 = new CyclicBarrier(2);

    @Override
    public void run() {
        marked[0] = true;
        for (int i = 0; i < 10000; i++) {
            cyclicBarrier2.reset();
            try {
                cyclicBarrier1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            index++;
            cyclicBarrier1.reset();
            try {
                cyclicBarrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            realIndex.incrementAndGet();
            synchronized (this) {
                if (marked[index] && marked[index - 1]) {
                    wrongCount.incrementAndGet();
                    System.out.println(String.format("发生错误...%s", index));
                }
                marked[index] = true;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentSecurityTask task = new ConcurrentSecurityTask();
        Thread threadA = new Thread(task, "Thread-A");
        Thread threadB = new Thread(task, "Thread-B");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println(String.format("index...%s", task.index));
        System.out.println(String.format("实际累加次数...%s", task.realIndex));
        System.out.println(String.format("出错次数...%s", task.wrongCount));
    }
}
