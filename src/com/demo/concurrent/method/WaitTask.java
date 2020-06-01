package com.demo.concurrent.method;

public class WaitTask implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        WaitTask waitTask = new WaitTask();
        Thread threadA = new Thread(waitTask, "Thread-A");
        Thread threadB = new Thread(waitTask, "Thread-B");
        Thread threadC = new Thread(() -> {
            synchronized (waitTask) {
                System.out.println(Thread.currentThread().getName() + "...notify");
                waitTask.notify();
                System.out.println(Thread.currentThread().getName() + "...notify finished");
            }
        }, "Thread-C");
        threadA.start();
        threadB.start();
//        Thread.sleep(100);
        threadC.start();
    }

    @Override
    public void run() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + "...wait");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "...wait finished");
        }
    }
}
