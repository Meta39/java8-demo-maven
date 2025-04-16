package com.fu.springbootfeignseata1.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 创建日期：2024-05-13
 */
@FeignClient(name = "spring-boot-feign-seata2", url = "127.0.0.1:8089")
public interface UserFeignClient {

    @GetMapping("user")
    void user();

}
