package com.fu.springbootdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * URI匹配规则
 */
@Slf4j
public class AntPathMatcherTests {
    @Test
    public void matcher(){

        List<String> uris = new ArrayList<>();
        uris.add("/api/test");
        uris.add("/api/demo");
        uris.add("/api/*/test");
        uris.add("/api/demo/*");

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean passUri = uris.stream().anyMatch(uri -> antPathMatcher.match(uri,"/api/test"));

        log.info("/api/test是否是滤地址：{}",passUri);
//        log.info("/a是否匹配/a/*：{}",antPathMatcher.match("/a/*","/a"));//false
//        log.info("/a/b是否匹配/a/*：{}",antPathMatcher.match("/a/*","/a/b"));//true
//        log.info("/a是否匹配/a/bc/def：{}",antPathMatcher.match("/a/bc/def","/a"));//false
//        log.info("/a/b/c是否匹配/a/*/def：{}",antPathMatcher.match("/a/*/def","/a/b/c"));//false
//        log.info("/a/b/def是否匹配/a/*/def：{}",antPathMatcher.match("/a/*/def","/a/b/def"));//true
    }

    @Test
    public void test(){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("*:login","GET:login");
        log.info("{}",match);
    }
}
