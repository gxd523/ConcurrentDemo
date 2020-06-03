package com.demo.concurrent.interview;

import java.util.ArrayList;
import java.util.List;

public class ProducerConsumerMode implements Runnable {
    private final BlockingQueue<Integer> blockingQueue;
    private final boolean isProducer;

    public ProducerConsumerMode(BlockingQueue<Integer> blockingQueue, boolean isProducer) {
        this.blockingQueue = blockingQueue;
        this.isProducer = isProducer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(0);// 0秒也会耗时一下，会影响被notify的拿到锁，还是下一个循环
                if (isProducer) {
                    blockingQueue.put(i);
                } else {
                    blockingQueue.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>();
        new Thread(new ProducerConsumerMode(blockingQueue, true), "Producer-1").start();
        new Thread(new ProducerConsumerMode(blockingQueue, false), "Consumer-1").start();
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

        public synchronized void take() throws InterruptedException {
            while (list.size() == 0) {// 注意这里不能改成if, 因为多线程时,如果只剩1件商品，接下来两个消费者一次获得锁，第二个消费者就会数组越界
                wait();
            }
            T id = list.remove(0);
            Thread.sleep(500);
            System.out.printf("%s消费了商品%s,\t仓库还剩%s个商品\n", Thread.currentThread().getName(), id, list.size());
            notifyAll();
        }
    }
}
