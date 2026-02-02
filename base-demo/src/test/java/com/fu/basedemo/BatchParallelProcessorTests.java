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
                if (i % 2 == 0) {
                    throw new RuntimeException(String.valueOf(i));
                }
                System.out.println("我是" + i);
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
