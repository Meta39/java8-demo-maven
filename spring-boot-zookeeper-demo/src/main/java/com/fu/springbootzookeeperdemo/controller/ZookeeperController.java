package com.fu.springbootzookeeperdemo.controller;

import com.fu.springbootzookeeperdemo.service.DistributedLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/zookeeper")
@RequiredArgsConstructor
public class ZookeeperController {
    private final DistributedLockService distributedLockService;
    private static final int defaultCount = 3;
    private int stock = defaultCount;

    /**
     * 重置共享参数
     */
    @GetMapping("/resetCount")
    public void resetCount() {
        stock = defaultCount;
        log.info("商品数量已重置为：{}", stock);
    }

    /**
     * 无锁
     */
    @GetMapping("/testLockFree")
    public void testLockFree() {
        //业务逻辑
        doSomething();
    }

    /**
     * 分布式锁
     */
    @GetMapping("/testLock")
    public void testLock() {
        distributedLockService.executeWithLock(this::doSomething);
    }

    private void doSomething() {
        if (stock > 0) {
            // 模拟耗时操作，增加线程切换概率
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock = stock - 1;                // ② 更新库存
            log.info("剩余：{}" , stock);
        } else {
            log.warn("库存不足:{}", stock);
        }
    }

}
