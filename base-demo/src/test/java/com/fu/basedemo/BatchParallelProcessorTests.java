package com.fu.basedemo;

import com.fu.basedemo.juc.util.BatchParallelProcessorUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BatchParallelProcessorTests {
    private static final Logger log = Logger.getLogger(BatchParallelProcessorTests.class.getName());

    /**
     * 批处理执行任务如果
     */
    @Test
    public void test() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Boolean> taskResultList = BatchParallelProcessorUtils.parallelProcess(list, 2, (subList) -> {
            for (int i : subList) {
                if (i == 5) {
                    throw new RuntimeException("我是会报错的:" + i);
                }
                log.info("我是" + i);
            }
            return true;
        });
        for (int i = 0; i < taskResultList.size(); i++) {
            if (taskResultList.get(i) == null) {
                log.info("第" + (i + 1) + "批次执行失败");
            }
        }
    }

    private void cancel() {

    }

}
