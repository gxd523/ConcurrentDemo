package com.demo.concurrent.deadlock;

public class TransferMoney {
    static void transferMoney(Account from, Account to, int amount) {
        synchronized (from) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(String.format(// 因为有打印IO这种耗时操作，所以这里不用加Thread.sleep(100)了
//                    "%s拿到%s锁, 等待%s锁", Thread.currentThread().getName(), from.hashCode() % 100, to.hashCode() % 100
//            ));
            synchronized (to) {
                if (from.balance < amount) {
                    System.out.println(String.format("account-%s 余额%s < %s, 余额不足, 转账失败!", from.accountId, from.balance, amount));
                    return;
                }
                from.balance -= amount;
                to.balance += amount;
                System.out.println(String.format("account-%s 成功给account-%s 转账%s元", from.accountId, to.accountId, amount));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account(1, 500);
        Account account2 = new Account(2, 500);

        Thread thread1 = new Thread(() -> transferMoney(account1, account2, 100), "Thread-1");
        Thread thread2 = new Thread(() -> transferMoney(account2, account1, 200), "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(String.format(
                "account-%s余额: %s, account-%s余额: %s",
                account1.accountId,
                account1.balance,
                account2.accountId,
                account2.balance
        ));
    }

    static final class Account {
        int accountId;
        int balance;

        public Account(int accountId, int balance) {
            this.accountId = accountId;
            this.balance = balance;
        }
    }
}
