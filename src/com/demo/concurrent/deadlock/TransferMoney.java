package com.demo.concurrent.deadlock;

public class TransferMoney {
    static void transferMoney(Account from, Account to, int amount) {
        synchronized (from) {
            System.out.println(String.format(// 因为有打印IO这种耗时操作，所以这里不用加Thread.sleep(100)了
                    "%s拿到%s锁, 等待%s锁", Thread.currentThread().getName(), from.hashCode() % 100, to.hashCode() % 100
            ));
            synchronized (to) {
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account(500);
        Account account2 = new Account(500);

        Thread thread1 = new Thread(() -> transferMoney(account1, account2, 100), "Thread-1");
        Thread thread2 = new Thread(() -> transferMoney(account2, account1, 200), "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(String.format("account1余额: %s, account2余额: %s", account1.balance, account2.balance));
    }

    static final class Account {
        int balance;

        public Account(int balance) {
            this.balance = balance;
        }
    }
}
