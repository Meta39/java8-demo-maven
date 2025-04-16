package com.fu.basedemo.juc;

import java.util.concurrent.Callable;

/**
 * 多线程之实现Callable接口，重写call方法，该方法有返回值。
 */
public class JavaThreadImplCallable implements Callable<Integer> {

    /**
     * 执行call()方法时，主线程是阻塞状态的。返回内容/抛出异常后解除阻塞
     */
    @Override
    public Integer call() throws Exception {
        Thread.sleep(3000);
        System.out.println("多线程之实现Runnable接口，重写call方法，该方法有返回值。返回值的类型自定义");
//        if (true){
//            //抛出异常，解除阻塞
//            throw new RuntimeException();
//        }
        return 1;
    }
}
