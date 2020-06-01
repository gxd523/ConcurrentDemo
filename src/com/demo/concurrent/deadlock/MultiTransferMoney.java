package com.demo.concurrent.deadlock;

import java.util.Random;

/**
 * 多线程连环死锁
 */
public class MultiTransferMoney {
    public static void main(String[] args) {
        final int ACCOUNT_COUNT = 500;
        final int ACCOUNT_BALANCE = 1000;

        TransferMoney.Account[] accounts = new TransferMoney.Account[ACCOUNT_COUNT];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new TransferMoney.Account(i, ACCOUNT_BALANCE);
        }

        Random random = new Random();
        Runnable transferMoneyTask = () -> {
            for (int i = 0; i < 999999; i++) {
                int fromAccount = random.nextInt(ACCOUNT_COUNT);
                int toAccount = random.nextInt(ACCOUNT_COUNT);
                int amount = random.nextInt(ACCOUNT_BALANCE);
                TransferMoney.transferMoney(accounts[fromAccount], accounts[toAccount], amount);
            }
            System.out.println(String.format("%s运行结束", Thread.currentThread().getName()));
        };

        for (int i = 0; i < 20; i++) {
            new Thread(transferMoneyTask).start();
        }
    }
}
