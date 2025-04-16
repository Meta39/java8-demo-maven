package com.fu.springbootsecuritydemo.mapper;

import com.fu.springbootsecuritydemo.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository
public interface LoginMapper {
    //查询用户角色集合信息
    Set<Role> selectUserRoleInfo(@Param("userId") Integer userId);

    //获取用户角色集合的权限集合
    Set<String> selectUserAuthorizes(@Param("roleIds") Set<Integer> roleIds);
}
