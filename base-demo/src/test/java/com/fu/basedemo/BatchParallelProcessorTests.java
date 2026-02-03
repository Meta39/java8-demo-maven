package com.fu.basedemo;

import com.fu.basedemo.juc.util.BatchParallelProcessorUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BatchParallelProcessorTests {

    /**
     * 批处理执行任务如果
     */
    @Test
    public void test() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Boolean> taskResultList = BatchParallelProcessorUtils.parallelProcess(list, 2, (subList) -> {
            for (int i : subList) {
                if (i == 5 || i == 7 || i == 9) {
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    throw new RuntimeException("我是会报错的:" + i);
                }
                System.out.println(Thread.currentThread().getName() + "我是" + i);
            }
            return true;
        });
        for (int i = 0; i < taskResultList.size(); i++) {
            if (taskResultList.get(i) == null) {
                System.out.println("第" + i + "批次执行失败");
            }
        }
    }

}
