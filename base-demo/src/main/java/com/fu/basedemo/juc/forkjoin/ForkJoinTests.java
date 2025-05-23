package com.fu.basedemo.juc.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join分支合并框架（二分法）：先将一个大的业务拆分成多个小的业务去执行，小的业务执行完毕之后，返回给大的业务结果。
 */
public class ForkJoinTests {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        //创建MyTask对象
        MyTask myTask = new MyTask(0, 100);
        //创建分支合并池对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        //获取最终合并的结果
        Integer result = forkJoinTask.get();
        System.out.println(result);
        //关闭池对象
        forkJoinPool.shutdown();
    }
}

//1.
class MyTask extends RecursiveTask<Integer> {
    //拆分差值不能超过VALUE，计算VALUE以内的运算。
    private static final Integer VALUE = 10;
    private final int begin; //拆分开始值
    private final int end; //拆分结束值
    private int result; //返回结果

    //2.创建有参构造函数
    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    //3.拆分合并过程
    @Override
    protected Integer compute() {
        //判断相加的2个数值是否大于VALUE
        if ((end - begin) <= VALUE) {
            //相加操作
            for (int i = begin; i <= end; i++) {
                result = result + i;
            }
        } else {//进一步拆分
            //获取中间值
            int middle = (begin + end) / 2;
            //拆分左边
            MyTask taskLeft = new MyTask(begin, middle);
            //拆分右边
            MyTask taskRight = new MyTask(middle + 1, end);
            //调用拆分方法
            taskLeft.fork();
            taskRight.fork();
            //合并结果
            result = taskLeft.join() + taskRight.join();
        }
        return result;
    }

}