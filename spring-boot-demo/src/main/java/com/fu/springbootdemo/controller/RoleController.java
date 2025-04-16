package com.fu.springbootdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.annotation.PreAuthorize;
import com.fu.springbootdemo.entity.Role;
import com.fu.springbootdemo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 角色
 */
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    /**
     * 根据ID查询角色
     *
     * @param id ID
     */
    @PreAuthorize("role:select")
    @GetMapping("{id}")
    public Role selectRoleById(@PathVariable("id") Integer id) {
        return this.roleService.selectRoleById(id);
    }

    /**
     * 根据角色ID获取角色的权限菜单集合
     */
    @PreAuthorize("role:select")
    @GetMapping("menu/{id}")
    public Map<String,Object> selectRoleMenuById(@PathVariable("id") Integer id) {
        return this.roleService.selectRoleMenuById(id);
    }

    /**
     * 新增角色
     *
     * @param role 角色实体类
     */
    @PreAuthorize("role:insert")
    @PostMapping
    public Integer insertRole(@RequestBody @Valid Role role) {
        return this.roleService.insertRole(role);
    }

    /**
     * 更新角色
     *
     * @param role 角色实体类
     */
    @PreAuthorize("role:update")
    @PutMapping
    public Integer updateRole(@RequestBody @Valid Role role) {
        return this.roleService.updateRole(role);
    }

    /**
     * 绑定角色所对应的权限
     * @param roleId 角色ID
     * @param authorizeIds 权限ID集合
     */
    @PreAuthorize("role:update")
    @PostMapping("roleMenu/{roleId}")
    public Integer roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> authorizeIds){
        return this.roleService.roleMenu(roleId,authorizeIds);
    }

    /**
     * 根据ID删除角色
     *
     * @param id ID
     */
    @PreAuthorize("role:delete")
    @DeleteMapping("{id}")
    public Integer deleteRole(@PathVariable Integer id) {
        return this.roleService.deleteRoleById(id);
    }

    /**
     * 查询角色 分页数据
     */
    @PreAuthorize("role:select")
    @GetMapping("page")
    public Page<Role> selectRolePage(@RequestParam(required = false, defaultValue = "1") Long page,
                                     @RequestParam(required = false, defaultValue = "10") Long size,
                                     @RequestParam("roleName") String roleName) {
        return this.roleService.selectRolePage(page, size , roleName);
    }

    /**
     * 查询角色 列表
     */
    @PreAuthorize("role:select")
    @GetMapping
    public List<Role> selectRoleList(Role role) {
        return this.roleService.selectRoleList(role);
    }

    /**
     * 根据ID集合批量删除角色
     */
    @PreAuthorize("role:delete")
    @DeleteMapping("deleteBatch")
    public Integer deleteRoles(@RequestBody List<Integer> ids) {
        return this.roleService.deleteRoleByIds(ids);
    }
}
