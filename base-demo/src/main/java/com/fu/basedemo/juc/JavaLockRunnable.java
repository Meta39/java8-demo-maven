package com.fu.basedemo.juc;

/**
 * 多线程减库存，模拟并发
 */
public class JavaLockRunnable implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
//            JavaLock.reduceCount();
//            JavaLock.synchronizedReduceCount();
            JavaLock.reentrantLockReduceCount();
        }
    }
}
