package com.fu.basedemo.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地锁失效场景
 * 1.多例模式
 * 2.事务：Read UnCommitted
 * 3.集群：利用nginx负载均衡访问集群
 */
public class JavaLock {
    /**
     * 库存
     */
    private static Integer productCount = 5000;
    /**
     * jdk锁：必须声明为static final，因为要确保多线程公用一个Lock实例。
     */
    private static final ReentrantLock lock = new ReentrantLock();

    private static final Thread thread1 = new Thread(new JavaLockRunnable(),"第1个线程");
    private static final Thread thread2 = new Thread(new JavaLockRunnable(),"第2个线程");
    private static final Thread thread3 = new Thread(new JavaLockRunnable(),"第3个线程");
    private static final Thread thread4 = new Thread(new JavaLockRunnable(),"第4个线程");
    private static final Thread thread5 = new Thread(new JavaLockRunnable(),"第5个线程");

    public static void main(String[] args) {
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }

    /**
     * 不推荐，不使用锁，线程不安全会导致库存数量有问题！
     */
    public static void reduceCount(){
        System.out.println("当前数量：" + productCount--);
    }

    /**
     * 推荐，使用synchronized锁住调用方法可以解决并发问题，性能较低
     */
    public synchronized static void synchronizedReduceCount(){
        System.out.println("当前数量：" + productCount--);
    }

    /**
     * 推荐，使用ReentrantLock锁住代码块也可以解决并发问题，性能较高
     */
    public static void reentrantLockReduceCount(){
        //加锁
        lock.lock();
        try {
            System.out.println("线程名称【"+Thread.currentThread().getName()+"】当前数量：" + productCount--);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            //必须在finally里解锁，防止死锁。
            lock.unlock();
        }
    }

}
