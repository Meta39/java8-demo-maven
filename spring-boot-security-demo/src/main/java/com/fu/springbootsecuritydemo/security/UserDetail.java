package com.fu.springbootsecuritydemo.security;

import com.fu.springbootsecuritydemo.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class UserDetail extends User implements UserDetails {
    private static final long serialVersionUID = -5140637827121772520L;
    private Set<GrantedAuthority> authorities;

    /**
     * 把User浅拷贝到UserDetail
     */
    public static UserDetail copyUser(User user){
        UserDetail instance = getInstance();
        //把user浅拷贝到instance
        BeanUtils.copyProperties(user,instance);
        return instance;
    }

    //创建饿汉式单例模式
    private UserDetail(){}
    private static final UserDetail userDetail = new UserDetail();
    public static UserDetail getInstance(){
        return userDetail;
    }

    /**
     * 设置权限信息
     */
    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDetail that = (UserDetail) o;
        return Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authorities);
    }

}

