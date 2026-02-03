package com.fu.basedemo;

import com.fu.basedemo.juc.util.BatchParallelProcessorUtils;
import com.fu.basedemo.juc.util.TaskResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class BatchParallelProcessorTests {
    private static final Logger log = Logger.getLogger(BatchParallelProcessorTests.class.getName());
    private static final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    /**
     * 批处理执行任务部分失败，取消失败的那一批次数据。
     */
    @Test
    public void test() {
        List<TaskResult<List<Integer>, Boolean>> taskResultList = BatchParallelProcessorUtils.parallelProcess(list, 2, (subList) -> {
            for (int i : subList) {
                if (i == 5 || i == 9) {
                    throw new RuntimeException("我是会报错的:" + i);
                }
                log.info("我是" + i);
            }
            return true;
        });
        log.info("部分取消开始....");
        for (TaskResult<List<Integer>, Boolean> taskResult : taskResultList) {
            if (!taskResult.getSuccess()) {
                cancel(taskResult.getUuid(), taskResult.getBatch());
            }
        }
        log.info("部分取消结束....");
    }

    /**
     * 批处理执行任务部分失败，全部取消
     */
    @Test
    public void test2() {
        List<TaskResult<List<Integer>, Boolean>> taskResultList = BatchParallelProcessorUtils.parallelProcess(list, 2, (subList) -> {
            for (int i : subList) {
                if (i == 5 || i == 9) {
                    throw new RuntimeException("我是会报错的:" + i);
                }
                log.info("我是" + i);
            }
            return true;
        });
        boolean hasFail = taskResultList.stream().anyMatch(t -> Objects.equals(false, t.getSuccess()));
        if (hasFail) {
            log.info("全部取消开始....");
            for (TaskResult<List<Integer>, Boolean> taskResult : taskResultList) {
                cancel(taskResult.getUuid(), taskResult.getBatch());
            }
            log.info("全部取消结束....");
        }
    }

    private void cancel(String uuid, List<Integer> batch) {
        log.warning("批次ID：" + uuid + "，执行失败，调用取消接口，取消这一批次的数据:" + batch);
    }

}
