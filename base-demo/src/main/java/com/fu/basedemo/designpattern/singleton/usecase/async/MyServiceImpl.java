package com.fu.basedemo.designpattern.singleton.usecase.async;

/**
 * @since 2024-07-25
 */
public class MyServiceImpl implements MyService {

    /**
     * 异步调用
     */
    @Async
    @Override
    public void asyncMethod() {
        String name = Thread.currentThread().getName();
        System.out.println("Executing async method - " + name);
        // 模拟耗时操作
        try {
            Thread.sleep(3000);
            System.out.println("Executing async method - " + name + " success.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 同步调用
     */
    @Override
    public void normalMethod() {
        System.out.println("Executing normal method - " + Thread.currentThread().getName());
    }

}
