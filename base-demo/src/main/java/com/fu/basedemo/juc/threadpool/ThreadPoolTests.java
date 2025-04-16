package com.fu.basedemo.juc.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDK线程池（不推荐，可能产生OOM，推荐自定义线程池。）
 */
public class ThreadPoolTests {
    //一池N线程，如果线程池满了，后续的等待，直到池里有空闲线程再处理
    @Test
    public void test(){
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);//一池5线程
        System.out.println("一池5线程，如：1个银行，5个柜台。");
        //模拟10个顾客请求
        try {
            for (int i = 0; i <= 10; i++) {
                //执行
                threadPool1.execute(() -> System.out.println(Thread.currentThread().getName()+"号窗口办理业务"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            //关闭
            threadPool1.shutdown();
        }
    }

    //一池一线程，如果当前线程有任务正在处理，后续的等待，直到有空闲线程再处理。
    @Test
    public void test2(){
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();//一池一线程
        System.out.println("一池一线程，如：1个银行，只有1个柜台可以使用。");
        try {
            for (int i = 0; i <= 10; i++) {
                //执行
                threadPool2.execute(() -> System.out.println(Thread.currentThread().getName()+"号窗口办理业务"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            //关闭
            threadPool2.shutdown();
        }
    }

    //一池可扩容线程
    @Test
    public void test3(){
        ExecutorService threadPool3 = Executors.newCachedThreadPool();//一池可扩容线程
        System.out.println("一池一线程，如：1个银行，只有多个柜台可以使用，但是有的可能窗口暂停业务，后又恢复办理业务。有的正常办理业务，后又暂停业务。");
        try {
            for (int i = 0; i <= 10; i++) {
                //执行
                threadPool3.execute(() -> System.out.println(Thread.currentThread().getName()+"号窗口办理业务"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            //关闭
            threadPool3.shutdown();
        }
    }
}
