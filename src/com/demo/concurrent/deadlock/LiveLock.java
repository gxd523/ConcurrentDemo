package com.demo.concurrent.deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LiveLock {
    private static void eatWith(Object spoon, Diner diner, Diner mate) throws InterruptedException {
        Random random = new Random();
        while (diner.isHungry) {
            TimeUnit.SECONDS.sleep(random.nextInt(5));

            if (mate.isHungry && random.nextInt(10) < 9) {
                System.out.printf("%s 让%s 先吃\n", diner.name, mate.name);
                continue;
            }

            synchronized (spoon) {
                System.out.printf("%s吃完了!\n", diner.name);
                diner.isHungry = false;
            }
        }
    }

    public static void main(String[] args) {
        Diner dinerA = new Diner("A");
        Diner dinerB = new Diner("B");
        Object spoon = new Object();

        new Thread(() -> {
            try {
                eatWith(spoon, dinerA, dinerB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                eatWith(spoon, dinerB, dinerA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class Diner {
        private final String name;
        private boolean isHungry;

        public Diner(String name) {
            this.name = name;
            isHungry = true;
        }
    }
}
