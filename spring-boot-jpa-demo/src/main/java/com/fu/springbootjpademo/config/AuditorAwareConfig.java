package com.fu.springbootjpademo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 创建日期：2024-06-26
 * 自动填充 @CreatedBy 创建者、 @LastModifiedBy 修改者数据
 */
@Configuration
public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //一般情况下通过 token 获取当前用户名填充。
        return Optional.of("创建者/修改者姓名");
    }

}
