package com.fu.springbootsecuritydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springbootsecuritydemo.entity.RoleAuthorize;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色关联权限 mapper映射
 *
 * @author meta39
 * @since 2023-03-13 21:36:01
 */
@Mapper
@Repository
public interface RoleAuthorizeMapper extends BaseMapper<RoleAuthorize> {

}
