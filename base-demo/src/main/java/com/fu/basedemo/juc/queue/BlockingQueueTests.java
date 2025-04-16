package com.fu.basedemo.juc.queue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 */
public class BlockingQueueTests {

    //创建一个定长的阻塞队列
    private static final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
    //抛出异常
    @Test
    public void test(){
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.remove()); //移除队列当中的元素，如果有则返回移除的元素，没有则抛出异常：java.util.NoSuchElementException。
//        System.out.println(blockingQueue.element()); //获取队列当中的元素，如果有则返回，没有则抛出异常：java.util.NoSuchElementException。
//        System.out.println(blockingQueue.add("d")); //当队列已满时，往队列里添加元素，则会抛出异常：Queue full。
    }

    //特殊值
    @Test
    public void test2(){
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d")); //往队列里面添加元素，超出队列长度则返回false。
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll()); //移除队列里面的元素，如果有元素，则返回元素，没有则返回null。
    }

    //阻塞
    @Test
    public void test3() throws InterruptedException {
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
//        blockingQueue.put("d"); //如果队列已经满了，则一直阻塞，直到队列有位置。
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take()); //如果队列没有元素，则一直阻塞，直到队列有元素。
    }

    //超时
    @Test
    public void test4() throws InterruptedException {
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d",3, TimeUnit.SECONDS)); //往队列里面添加元素，如果队列已满，则等待指定时间再次往队列里面设置元素，设置成功返回true，失败返回false。
        System.out.println(blockingQueue.poll(3,TimeUnit.SECONDS)); //移除队列里面的元素，如果队列里面有元素，则返回元素，没有则等待指定时间再次移除队列里面的元素，如果有元素则返回元素，没有则返回null
    }

}
