package com.fu.basedemo.juc;


/**
 * 多线程之继承Thread，重写run方法，该方法无返回值。
 */
public class JavaThreadExtendsThread extends Thread{

    @Override
    public void run() {
        //关于interrupted()方法，如果一个线程的 run() 方法执行一个无限循环，并且没有执行 sleep() 等会抛出 InterruptedException 的操作，那么调用线程的 interrupt() 方法就无法使线程提前结束。
        //但是调用 interrupt() 方法会设置线程的中断标记，此时调用 interrupted() 方法会返回 true。因此可以在循环体中使用 interrupted() 方法来判断线程是否处于中断状态，从而提前结束线程。
        while (!interrupted()) {
            System.out.println("循环......");
        }
        super.setName("继承Thread类，无返回值");
        System.out.println("多线程之继承Thread，重写run方法，该方法无返回值。");
    }

}
