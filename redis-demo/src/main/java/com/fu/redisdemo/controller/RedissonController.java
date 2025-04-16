package com.fu.redisdemo.controller;

import com.fu.redisdemo.util.RedissonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RequestMapping("/redisson")
@RestController
@RequiredArgsConstructor
public class RedissonController {
    private final RedissonClient redisson;
    private static final String STOCKS = "stocks";//库存key的名称
    private static final Long TOTAL_STOCKS = 100L;//库存总数
    private static final Long ACTIVITY_DAYS = 1L;//活动天数
    private int i = 10;

    /**
     * 不加锁，多线程并发操作减扣商品引出超卖问题
     */
    @PostMapping("/subProductNoLock")
    public void subProductNoLock() {
        if (i == 0) {
            log.info("卖完了~");
            return;
        }
        log.info("您抢到了第" + i + "个免单券！");
        i--;
    }

    /**
     * 加分布式锁，解决多线程并发操作减扣商品超卖问题
     */
    @PostMapping("/subProductLock")
    public void subProductLock() {
        boolean lock = RedissonUtils.lock(redisson, "subProductLock", () -> {
            RAtomicLong stocks = redisson.getAtomicLong(STOCKS);
            long cunrrent = stocks.get();//获取当前值
            if (cunrrent == 0L) {
                log.info("卖完了~");
                return;
            }
            log.info("您抢到了第" + cunrrent + "个免单券！");
            stocks.decrementAndGet();//原子递减 1 并返回新值
        });
        if (!lock) {
            log.info("哎呀~活动太火爆了~请稍后再试");
        }
    }

    /**
     * 重置库存
     */
    @GetMapping("/reset")
    public void reset() {
        // 使用 Redis 命令原子化设置值和过期时间，其它复合操作也适用。
        RBatch batch = redisson.createBatch();
        batch.getAtomicLong(STOCKS).compareAndSetAsync(0L, TOTAL_STOCKS);  // 库存为0，才设置值
        batch.getBucket(STOCKS).expireAsync(Duration.ofDays(ACTIVITY_DAYS));  // 设置活动时间为1天
        batch.execute();
    }

}
