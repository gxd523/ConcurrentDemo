package com.demo.concurrent.jmm;

/**
 * 可见性问题演示
 */
public class FieldVisibility {
    int a = 1;
    int b = 2;

    private void change() {
        a = 3;
        b = a;
    }

    /**
     * 不要加if半段之类的
     * System.out不具有原子性，可能执行一半切换到change()，所以不能先打印a
     *
     * b = 3, a = 1
     * print()所在线程只同步了b的值，没同步a的值
     */
    private void print() {
        System.out.printf("b = %s, a = %s\n", b, a);
    }

    public static void main(String[] args) {
        while (true) {
            FieldVisibility test = new FieldVisibility();
            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.change();
            }).start();

            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.print();
            }).start();
        }
    }
}
