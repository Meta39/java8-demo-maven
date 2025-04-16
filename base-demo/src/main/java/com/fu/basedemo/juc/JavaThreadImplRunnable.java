package com.fu.basedemo.juc;

/**
 * 多线程之实现Runnable接口，重写run方法，该方法无返回值。
 */
public class JavaThreadImplRunnable implements Runnable {

    /**
     * sleep方法会休眠当前正在执行的线程，单位为毫秒。注意：一般不会在run方法里面休眠。这里只是演示！！！
     */
    private static final int sleepTime = 3000;

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("多线程之实现Runnable接口，重写run方法，该方法无返回值。使用sleep方法会休眠当前正在执行的线程" + sleepTime + "毫秒。注意：一般不会在run方法里面休眠。这里只是演示！！！。");
        //对静态方法Thread.yield()的调用声明了当前线程已经完成了生命周期中最重要的部分，可以切换给其它线程来执行。该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其它线程可以运行。
        Thread.yield();
    }
}