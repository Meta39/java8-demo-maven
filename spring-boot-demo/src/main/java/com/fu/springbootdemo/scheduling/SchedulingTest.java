package com.fu.springbootdemo.scheduling;

import com.fu.springbootdemo.async.AsyncThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingTest {
    private final AsyncThread asyncThread;

    @Scheduled(fixedDelay = 60_000L)
    public void scheduled() {
        log.info("scheduled invoke...");
        asyncThread.asyncThread();
    }

}
