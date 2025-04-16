package com.fu.basedemo.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用多线程的三种方法
 * 实现 Runnable 接口，重写run方法；
 * 实现 Callable 接口，重写call方法；
 * 继承 Thread 类，重写run方法。
 */
public class JavaThreadTest {
    /*
       放到外面只会生成一个实例，放到方法内会生成多个实例。
     */
    /**
     * 推荐，因为java是单继承，多实现。
     */
    private static final Thread implRunnable = new Thread(new JavaThreadImplRunnable(), "实现Runnable接口，无返回值");
    /**
     * 不推荐，因为类可能只要求可执行就行，继承整个 Thread 类开销过大
     */
    private static final Thread extendsThread = new JavaThreadExtendsThread();
    /**
     * 推荐，一般多线程不会用有返回值的
     */
    private static final JavaThreadImplCallable implCallableObj = new JavaThreadImplCallable();
    private static final FutureTask<Integer> ft = new FutureTask<>(implCallableObj);
    private static final Thread implCallable = new Thread(ft, "实现Callable接口，有返回值");

    @Test
    public void test() throws ExecutionException, InterruptedException {
        //start方法启动线程，实现Runnable接口重写run方法里面使用了Thread.sleep()方法，因此该线程会休眠。
        implRunnable.start();

        extendsThread.start();
        //通过interrupt()方法执行无限循环的控制，防止死循环。
        Thread.sleep(1);//如果这里不设置休眠时间，会看不到信息输出。
        extendsThread.interrupt();//interrupt()的使用比较危险，所以尽量不要在run方法里面使用无限循环。

        implCallable.start();//启动Callable后，主线程默认是阻塞的，直到子线程返回内容或者抛出异常，才解除阻塞。
        System.out.println("3秒后获取Callable接口返回值：" + ft.get());
        //使用lambda创建一个线程并启动
        new Thread(() -> {
            System.out.println("使用lambda表达式创建一个线程并执行");
        }).start();
    }
}