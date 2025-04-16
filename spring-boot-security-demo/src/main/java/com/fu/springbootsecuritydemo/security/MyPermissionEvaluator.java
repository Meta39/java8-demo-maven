package com.fu.springbootsecuritydemo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.info("authentication:{}",authentication);
        log.info("targetDomainObject:{}",targetDomainObject);
        log.info("permission:{}",permission);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities != null && !authorities.isEmpty()){
            return authorities.stream().anyMatch(authorize -> authorize.toString().equals(permission));
        }
        return false;
    }

    /**
     * 这个是没用的
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        log.info("2authentication:{}",authentication);
        log.info("targetId:{}",targetId);
        log.info("targetType:{}",targetType);
        log.info("2permission:{}",permission);
        return false;
    }
}
