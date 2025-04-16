package com.fu.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springbootdemo.entity.UserRole;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户关联角色 mapper映射
 *
 * @author meta39
 * @since 2023-03-13 21:36:02
 */
@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
