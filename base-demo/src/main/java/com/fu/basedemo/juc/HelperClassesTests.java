package com.fu.basedemo.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 辅助类：CountDownLatch减少计数、CyclicBarrier循环栅栏、Semaphore信号灯
 */
public class HelperClassesTests {

    /**
     * CountDownLatch减少计数：每次减一，直到减为0。
     * PS：和CyclicBarrier相反
     * 应用场景：流程控制
     */
    @Test
//    public static void main(String[] args) {
    public void test() throws InterruptedException {
        //传一个数量，即：这个数量每执行一次会减一。直到0。
        CountDownLatch countDownLatch = new CountDownLatch(5);
        System.out.println("除班长以外有5名同学。");
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "号同学离开了教室");
                //计数 -1
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        // 等待   直到countDownLatch减为0
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "班长锁门。");
    }

    /**
     * CyclicBarrier循环栅栏：每次加一，直到固定值。
     * PS：和CountDownLatch相反
     * 应用场景：批量操作，达到多少条才执行批量操作。
     */
    @Test
//    public static void main(String[] args) {
    public void test2() {
        int number = 7; //创建固定值
        CyclicBarrier cyclicBarrier = new CyclicBarrier(number, () -> System.out.println("集齐七龙珠召唤神龙许愿。"));
        //集齐龙珠过程
        for (int i = 1; i <= number; i++) {
            new Thread(() -> {
                try {
                    System.out.println("收集到" + Thread.currentThread().getName() + "星龙珠");
                    //等待
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }, String.valueOf(i)).start();
        }
    }

    /**
     * Semaphore信号灯
     * 应用场景：线程池数量、消息队列数量，总数只有这么多，超出了的就进行等待，直到前一个释放，其它才能竞争。
     */
    public static void main(String[] args) {
        //创建Semaphore，设置许可数量
        Semaphore semaphore = new Semaphore(3);
        System.out.println("6辆汽车，停3个车位");
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                try {
                    //抢占车位
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "号汽车抢到了车位。");
                    //设置随机停车时间
                    TimeUnit.SECONDS.sleep(current.nextInt(5));
                    System.out.println(Thread.currentThread().getName() + "号汽车离开了车位。");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放车位
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }

}
