package com.demo.concurrent.deadlock;

import java.util.Random;

/**
 * 哲学家就餐问题
 */
public class DiningPhilosophers {
    static class Philosopher implements Runnable {
        private final Object leftChopstick;
        private final Object rightChopstick;

        public Philosopher(Object leftChopstick, Object rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAction("Thinking");
                    synchronized (leftChopstick) {
                        doAction("Picked up left chopstick.");
                        synchronized (rightChopstick) {
                            doAction("Picked up right chopstick and eating...");
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(String.format("%s...%s", Thread.currentThread().getName(), action));
            Thread.sleep(new Random().nextInt(10));
        }
    }

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[philosophers.length];

        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }
        for (int i = 0; i < philosophers.length; i++) {
            Object leftChopstick = chopsticks[i];
            Object rightChopstick = chopsticks[(i + 1) % chopsticks.length];
            boolean isLastPhilosopher = i == philosophers.length - 1;
            philosophers[i] = new Philosopher(// 解法一：最后一个哲学家先拿右筷，再拿左筷，避免形成死锁环，属于避免策略
                    isLastPhilosopher ? rightChopstick : leftChopstick,
                    isLastPhilosopher ? leftChopstick : rightChopstick
            );

            new Thread(philosophers[i], String.format("哲学家-%s号", i)).start();
        }
    }
}
