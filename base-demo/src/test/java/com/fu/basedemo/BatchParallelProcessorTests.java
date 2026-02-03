package com.fu.basedemo;

import com.fu.basedemo.juc.util.BatchParallelProcessorUtils;
import com.fu.basedemo.juc.util.TaskResult;
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
        List<TaskResult<List<Integer>, Boolean>> taskResultList = BatchParallelProcessorUtils.parallelProcess(list, 2, (subList) -> {
            for (int i : subList) {
                if (i == 5 || i == 9) {
                    throw new RuntimeException("我是会报错的:" + i);
                }
                log.info("我是" + i);
            }
            return true;
        });
        for (TaskResult<List<Integer>, Boolean> taskResult : taskResultList) {
            if (!taskResult.getSuccess()) {
                cancel(taskResult.getUuid(), taskResult.getBatch());
            }
        }
    }

    private void cancel(String uuid, List<Integer> batch) {
        log.warning("批次ID：" + uuid + "，执行失败，调用取消接口，取消这一批次的数据:" + batch);
    }

}
