package com.demo.concurrent.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private String name;

    public MyUncaughtExceptionHandler(String name) {
        this.name = name;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.WARNING, String.format("%s终止了，%s捕获到异常如下：", Thread.currentThread().getName(), name), e);
    }
}
