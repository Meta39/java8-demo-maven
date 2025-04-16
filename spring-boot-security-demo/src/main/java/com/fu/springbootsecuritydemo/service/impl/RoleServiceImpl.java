package com.fu.springbootsecuritydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fu.springbootsecuritydemo.entity.Authorize;
import com.fu.springbootsecuritydemo.entity.Role;
import com.fu.springbootsecuritydemo.entity.RoleAuthorize;
import com.fu.springbootsecuritydemo.mapper.AuthorizeMapper;
import com.fu.springbootsecuritydemo.mapper.RoleAuthorizeMapper;
import com.fu.springbootsecuritydemo.mapper.RoleMapper;
import com.fu.springbootsecuritydemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleAuthorizeMapper roleAuthorizeMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;

    /**
     * 根据ID查询角色并获取其权限列表
     */
    @Override
    public Role selectRoleById(Integer id) {
        if (id == 1){
            throw new RuntimeException("超级管理员默认拥有全权限，无需查询");
        }
        Role role = this.roleMapper.selectById(id);
        Set<Authorize> authorizes = new HashSet<>();
        this.roleAuthorizeMapper.selectList(new LambdaQueryWrapper<RoleAuthorize>().eq(RoleAuthorize::getRoleId, role.getId()))
                .forEach(roleAuthorize -> {
                    Authorize authorize = this.authorizeMapper.selectById(new LambdaQueryWrapper<Authorize>().eq(Authorize::getId, roleAuthorize.getAuthorizeId()));
                    authorizes.add(authorize);
                });
        role.setAuthorizes(authorizes);
        return role;
    }

    /**
     * 新增角色
     */
    @Override
    public int insertRole(Role role) {
        return this.roleMapper.insert(role);
    }

    /**
     * 更新角色
     */
    @Override
    public int updateRole(Role role) {
        return this.roleMapper.updateById(role);
    }

    /**
     * 根据ID删除角色
     */
    @Override
    public int deleteRoleById(Integer id) {
        return this.roleMapper.deleteById(id);
    }

    /**
     * 分页查询角色
     */
    @Override
    public Page<Role> selectRolePage(Long page, Long size) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        return this.roleMapper.selectPage(Page.of(page, size), lqw);
    }

    /**
     * 查询角色
     * 列表
     */
    @Override
    public List<Role> selectRoleList(Role role) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        return this.roleMapper.selectList(lqw);
    }

    /**
     * 根据ID集合批量删除角色
     */
    @Override
    public int deleteRoleByIds(List<Integer> ids) {
        return this.roleMapper.deleteBatchIds(ids);
    }

}
