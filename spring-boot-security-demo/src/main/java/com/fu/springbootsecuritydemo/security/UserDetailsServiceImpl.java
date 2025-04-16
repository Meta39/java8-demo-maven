package com.fu.springbootsecuritydemo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fu.springbootsecuritydemo.entity.Role;
import com.fu.springbootsecuritydemo.entity.User;
import com.fu.springbootsecuritydemo.mapper.LoginMapper;
import com.fu.springbootsecuritydemo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "userDetailsService") //bean name必须叫这个
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过Security认证以后，设置当前登录用户权限等信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        Set<Role> roles = this.loginMapper.selectUserRoleInfo(user.getId());
        Set<GrantedAuthority> permission = new HashSet<>();
        if (roles != null && !roles.isEmpty()) {//角色集合可能为空，因为没有授予任何角色。
            roles.forEach(role -> permission.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())));//用于@PreAuthorize("hasRole('admin')")
            Set<String> authorities = this.loginMapper.selectUserAuthorizes(roles.stream().map(Role::getId).collect(Collectors.toSet()));
            if (authorities != null && !authorities.isEmpty()) {
                authorities.forEach(authorize -> permission.add(new SimpleGrantedAuthority("authorize")));//用于@PreAuthorize("hasPermission('','user:hello')")
            }
        }
        log.info("用户权限信息集合为：{}", permission);
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), permission);
    }
}
