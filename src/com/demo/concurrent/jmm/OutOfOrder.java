package com.demo.concurrent.jmm;

/**
 * 重排序
 */
public class OutOfOrder {
    final int x;
    int y;

    public OutOfOrder() {
        x = 1;
        y = 2;
    }

    public static void main(String[] args) {
        int n = 0;
        while (n < 99999) {
            new Thread(OutOfOrder::create).start();
            new Thread(OutOfOrder::read).start();
            n++;
        }
    }

    static OutOfOrder mOutOfOrder;

    static void create() {
        mOutOfOrder = new OutOfOrder();
    }

    static void read() {
        if (mOutOfOrder != null) {
            int x = mOutOfOrder.x;
            int y = mOutOfOrder.y;
            if (x != 1 || y != 2) {
                System.out.printf("x = %s, y = %s\n", x, y);
            }
        }
    }
}
