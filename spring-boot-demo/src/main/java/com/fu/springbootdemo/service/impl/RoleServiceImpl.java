package com.fu.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fu.springbootdemo.entity.Authorize;
import com.fu.springbootdemo.entity.Role;
import com.fu.springbootdemo.entity.RoleAuthorize;
import com.fu.springbootdemo.mapper.AuthorizeMapper;
import com.fu.springbootdemo.mapper.RoleAuthorizeMapper;
import com.fu.springbootdemo.mapper.RoleMapper;
import com.fu.springbootdemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleAuthorizeMapper roleAuthorizeMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;

    /**
     * 根据ID查询角色
     */
    @Override
    public Role selectRoleById(Integer id) {
        return this.roleMapper.selectById(id);
    }

    /**
     * 根据角色ID获取角色的权限菜单集合
     */
    @Override
    public Map<String,Object> selectRoleMenuById(Integer id) {
        Map<String,Object> result = new HashMap<>();
        List<Integer> authorizeIds;
        List<Authorize> authorizes = new ArrayList<>();
        if (id == 1){
            authorizes = this.authorizeMapper.selectList(null);
        }else {
            for (RoleAuthorize roleAuthorize : this.roleAuthorizeMapper.selectList(new LambdaQueryWrapper<RoleAuthorize>().eq(RoleAuthorize::getRoleId, id))) {
                Authorize authorize = this.authorizeMapper.selectOne(new LambdaQueryWrapper<Authorize>().eq(Authorize::getId, roleAuthorize.getAuthorizeId()));
                authorizes.add(authorize);
            }
        }
        authorizeIds = authorizes.stream().map(Authorize::getId).collect(Collectors.toList());
        List<Authorize> finalAuthorizes = authorizes;
        authorizes = authorizes.stream().filter(authorize -> authorize.getPId() == 0).peek(authorize -> authorize.setChildAuthorize(authorizeTree(authorize, finalAuthorizes))).collect(Collectors.toList());
        result.put("authorizeIds",authorizeIds);
        result.put("authorizes",authorizes);
        return result;
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
        if (Objects.nonNull(role) && role.getId() == 1){
            throw new RuntimeException("禁止修改超级管理员角色信息");
        }
        return this.roleMapper.updateById(role);
    }

    @Transactional
    @Override
    public int roleMenu(Integer roleId, List<Integer> authorizeIds) {
        if (roleId == 1){
            throw new RuntimeException("禁止给超级管理员分配菜单权限，因为超级管理员默认拥有全部权限！");
        }
        //省事做法：不管之前有没有分配权限，都把之前的该角色的权限删光。再从新插入权限。
        this.roleAuthorizeMapper.delete(new LambdaQueryWrapper<RoleAuthorize>().eq(RoleAuthorize::getRoleId,roleId));
        if (!authorizeIds.isEmpty()){
            authorizeIds.forEach(authorizeId ->{
                RoleAuthorize roleAuthorize = new RoleAuthorize();
                roleAuthorize.setRoleId(roleId);
                roleAuthorize.setAuthorizeId(authorizeId);
                this.roleAuthorizeMapper.insert(roleAuthorize);
            });
        }
        return 1;
    }

    /**
     * 根据ID删除角色
     */
    @Override
    public int deleteRoleById(Integer id) {
        if (id == 1){
            throw new RuntimeException("禁止删除超级管理员角色！");
        }
        return this.roleMapper.deleteById(id);
    }

    /**
     * 分页查询角色
     */
    @Override
    public Page<Role> selectRolePage(Long page, Long size,String roleName) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)){
            lqw.like(Role::getRoleName,roleName);
        }
        lqw.orderByAsc(Role::getId);
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
        if (ids != null && !ids.isEmpty() && ids.stream().anyMatch(id -> id == 1)){
            throw new RuntimeException("禁止删除超级管理员角色！");
        }
        return this.roleMapper.deleteBatchIds(ids);
    }

    //============================内部方法=======================================

    /**
     * 递归权限树
     */
    private static List<Authorize> authorizeTree(Authorize authorize, List<Authorize> authorizes) {
        return authorizes.stream()
                .filter(a ->
                        // NodeType：1是文件夹、2是页面、3是按钮，这里准确的说是返回按钮权限给前端展示。
                        (a.getPId().equals(authorize.getId())))
                .peek(a -> a.setChildAuthorize(authorizeTree(a, authorizes)))
                .collect(Collectors.toList());
    }

}
