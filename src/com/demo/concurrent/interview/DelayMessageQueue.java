package com.demo.concurrent.interview;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayMessageQueue {
    static class Message implements Delayed {
        private final long delayMillis;
        private final long initTime;

        public Message(long delayMillis) {
            this.delayMillis = delayMillis;
            initTime = System.currentTimeMillis();
        }

        @Override
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(delayMillis + initTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return String.format("%s在第%s秒后收到消息", Thread.currentThread().getName(), (System.currentTimeMillis() - initTime) / 1000);
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Message> queue = new DelayQueue<>();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    System.out.printf("%s发送一个延迟%s秒的消息\n", Thread.currentThread().getName(), i);
                    queue.put(new Message(i * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("........生产完成");
        }, "producer");
        producer.start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Message message = queue.take();
                    System.out.println(message.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumer").start();
    }
}