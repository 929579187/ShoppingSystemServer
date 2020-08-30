package com.sy.shopping.test;

public class TestThreadLocal {
    private static ThreadLocal<Integer> curCount = new ThreadLocal<>();

    /**
     * ThreadLocal:可以对变量产生一个副本对象（对每个线程都会创建该变量的副本）
     * 每个线程中对自己的副本进行操作并不会影响其他的线程
     * <p>
     * =>可以在Web开发中将req，res对象放入ThreadLocal中
     * 因为在web开发中一个请求其本质就是一个线程，所以如果把这些和Servlet相关的对象放入ThreadLocal中
     * 可以使得到本次请求过程中，无论是Handler，Service，Dao，工具类，都可以拿到当前和Servlet相关的对象
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程执行curCount.set(1):");
        curCount.set(1);
        new Thread(() -> {
            System.out.println("子线程执行curCount.get():");
            System.out.println(curCount.get());
            System.out.println("子线程执行curCount.set(2):");
            curCount.set(2);
            System.out.println("子线程执行curCount.get():");
            System.out.println(curCount.get());
            get();
        }).start();
        Thread.sleep(3000);
        System.out.println("主线程执行curCount.get():");
        System.out.println(curCount.get());
        get();
    }

    public static void get() {
        System.out.println("method get:" + curCount.get());
    }
}
