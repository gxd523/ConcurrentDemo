package com.demo.concurrent.deadlock;

public class TransferMoney {
    static void transferMoney(Account from, Account to, int amount) {
        synchronized (from) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (to) {
                transfer(from, to, amount);
            }
        }
    }

    /**
     * 修复了死锁问题
     */
    static void transferMoneyFixed(Account from, Account to, int amount) {
        int fromHashCode = System.identityHashCode(from);
        int toHashCode = System.identityHashCode(to);
        Object outerLock = fromHashCode > toHashCode ? from : to;
        Object innerLock = fromHashCode > toHashCode ? to : from;

        synchronized (outerLock) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (innerLock) {
                transfer(from, to, amount);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account(1, 500);
        Account account2 = new Account(2, 500);

        Thread thread1 = new Thread(() -> transferMoneyFixed(account1, account2, 100), "Thread-1");
        Thread thread2 = new Thread(() -> transferMoneyFixed(account2, account1, 200), "Thread-2");

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

    private static void transfer(Account from, Account to, int amount) {
        if (from.balance < amount) {
            System.out.println(String.format("account-%s 余额%s < %s, 余额不足, 转账失败!", from.accountId, from.balance, amount));
            return;
        }
        from.balance -= amount;
        to.balance += amount;
        System.out.println(String.format("account-%s 成功给account-%s 转账%s元", from.accountId, to.accountId, amount));
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
