package com.demo.concurrent.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产、消费不能同时进行
 * 产品剩10个时，停止生产
 */
public class ProducerConsumerMode {
    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(0);// 0秒也会耗时一下，会影响被notify的拿到锁，还是下一个循环
                    blockingQueue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer-1").start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(0);// 0秒也会耗时一下，会影响被notify的拿到锁，还是下一个循环
                    blockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer-1").start();
    }

    static class BlockingQueue<T> {
        private final List<T> list = new ArrayList<>();

        public synchronized void put(T id) throws InterruptedException {
            while (list.size() == 10) {// 注意这里不能改成if
                wait();
            }
            list.add(id);
            Thread.sleep(500);
            System.out.printf("%s生产了商品%s,\t仓库还剩%s个商品\n", Thread.currentThread().getName(), id, list.size());
            notifyAll();
        }

        /**
         * 注意while不能改成if, 如果2个消费者都在这里wait，接着生产者生产了1个商品，
         * 此时2个消费者都被唤醒，但只有一个能拿到锁并消费了商品后，另一个消费者再拿锁，此时已没有商品了
         */
        public synchronized void take() throws InterruptedException {
            while (list.size() == 0) {
                wait();
            }
            T id = list.remove(0);
            Thread.sleep(500);
            System.out.printf("%s消费了商品%s,\t仓库还剩%s个商品\n", Thread.currentThread().getName(), id, list.size());
            notifyAll();
        }
    }
}
