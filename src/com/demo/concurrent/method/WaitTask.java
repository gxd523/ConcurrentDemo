package com.demo.concurrent.method;

public class WaitTask {
    public static void main(String[] args) throws InterruptedException {
        Runnable waitTask = new Runnable() {// waitTask对象也是一把锁
            @Override
            public synchronized void run() {
                System.out.println(Thread.currentThread().getName() + "...wait");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "...wait finished");
            }
        };

        new Thread(waitTask, "Thread-A").start();
        new Thread(waitTask, "Thread-B").start();

        Thread.sleep(1000);

        new Thread(() -> {
            synchronized (waitTask) {
                System.out.println(Thread.currentThread().getName() + "...notify");
                waitTask.notifyAll();
                System.out.println(Thread.currentThread().getName() + "...notify finished");
            }
        }, "Thread-C").start();
    }
}
