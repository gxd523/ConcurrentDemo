package com.demo.concurrent.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolParameter {
    public static void main(String[] args) {
        int corePoolSize = 1;
        int maxPoolSize = 2;
        long keepTime = 10L;
        int capacity = 4;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(capacity);
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread-" + count++);
            }
        };
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepTime, TimeUnit.SECONDS, workQueue, threadFactory, handler);
        Runnable task = new Runnable() {
            final AtomicInteger count = new AtomicInteger(0);

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(count.incrementAndGet() + "...task..." + Thread.currentThread().getName());
            }
        };

        for (int i = 0; i < 999; i++) {
            threadPool.execute(task);
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                System.out.println(threadPool.getActiveCount() + "..." + threadPool.getTaskCount() + "..." + threadPool.getCompletedTaskCount()), 0, 5, TimeUnit.SECONDS);


//        new Thread(() -> {
//            Future<Integer> future = threadPool.submit(() -> {
//                Thread.sleep(5000);
//                return 998;
//            });
//            try {
//                System.out.println(future.get());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println(".....");
//        }).start();
    }
}
