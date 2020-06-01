package com.demo.concurrent.interview;

import java.util.ArrayList;
import java.util.List;

public class ProducerConsumerMode {
    public static void main(String[] args) {
        Storage storage = new Storage();
        Producer producer = new Producer(storage);
        Consumer consumer = new Consumer(storage);
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    static class Producer implements Runnable {
        private final Storage storage;

        public Producer(Storage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    storage.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final Storage storage;

        public Consumer(Storage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    storage.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Storage {
        private final List<Integer> list = new ArrayList<>();

        public synchronized void put(int id) throws InterruptedException {
            while (list.size() == 10) {// 注意这里不能改成if
                wait();
            }
            list.add(id);
            Thread.sleep(100);
            System.out.println(String.format("生产了商品%s,\t仓库还剩%s个商品", id, list.size()));
            notifyAll();
        }

        public synchronized void take() throws InterruptedException {
            while (list.size() == 0) {// 注意这里不能改成if, 因为多线程时,如果只剩1件商品，接下来两个消费者一次获得锁，第二个消费者就会数组越界
                wait();
            }
            int id = list.remove(0);
            Thread.sleep(50);
            System.out.println(String.format("消费了商品%s,\t仓库还剩%s个商品", id, list.size()));
            notifyAll();
        }
    }
}
