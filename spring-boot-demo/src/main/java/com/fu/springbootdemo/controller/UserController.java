package com.fu.springbootdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.annotation.PreAuthorize;
import com.fu.springbootdemo.entity.User;
import com.fu.springbootdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 根据ID查询用户
     *
     * @param id ID
     */
    @PreAuthorize("user:select")
    @GetMapping("{id}")
    public User selectUserById(@PathVariable("id") Integer id) {
        return this.userService.selectUserById(id);
    }

    /**
     * 通过Token获取用户信息
     */
    @GetMapping("getUserInfoByToken")
    public User getUserInfoByToken() {
        return this.userService.getUserInfoByToken();
    }

    /**
     * 新增用户
     *
     * @param user 用户实体类
     */
    @PreAuthorize("user:insert")
    @PostMapping
    public Integer insertUser(@RequestBody @Valid User user) {
        return this.userService.insertUser(user);
    }

    /**
     * 更新用户
     *
     * @param user 用户实体类
     */
    @PreAuthorize("user:update")
    @PutMapping
    public Integer updateUser(@RequestBody @Valid User user) {
        return this.userService.updateUser(user);
    }

    /**
     * 给用户分配角色
     */
    @PreAuthorize("user:update")
    @PutMapping("shareRoles/{userId}")
    public Integer shareRoles(@PathVariable Integer userId, @RequestBody List<Integer> roleIds) {
        return this.userService.shareRoles(userId,roleIds);
    }

    /**
     * 禁用用户
     * @param id 用户ID
     * @param isBan 是否禁用
     */
    @PreAuthorize("user:update")
    @PutMapping("isBan/{id}")
    public Integer isBanUser(@PathVariable Integer id,@RequestParam("isBan") Integer isBan) {
        return this.userService.isBanUser(id,isBan);
    }

    /**
     * 根据ID删除用户
     *
     * @param id ID
     */
    @PreAuthorize("user:delete")
    @DeleteMapping("{id}")
    public Integer deleteUser(@PathVariable Integer id) {
        return this.userService.deleteUserById(id);
    }

    /**
     * 查询用户 分页数据
     */
    @PreAuthorize("user:select")
    @GetMapping("page")
    public Page<User> selectUserPage(@RequestParam(required = false, defaultValue = "1") Long page,
                                     @RequestParam(required = false, defaultValue = "10") Long size,
                                     @RequestParam(required = false,name = "username") String username) {
        return this.userService.selectUserPage(page, size,username);
    }

    /**
     * 根据ID集合批量删除用户
     */
    @PreAuthorize("user:delete")
    @DeleteMapping("deleteBatch")
    public Integer deleteUsers(@RequestBody List<Integer> ids) {
        return this.userService.deleteUserByIds(ids);
    }

    /**
     * 导入Excel
     */
    @PreAuthorize("user:importExcel")
    @PostMapping("importExcel")
    public Boolean importExcel(@RequestParam(name = "file") MultipartFile file){
        return this.userService.importExcel(file);
    }

    /**
     * 导出Excel
     */
    @PreAuthorize("user:exportExcel")
    @PostMapping("exportExcel")
    public String exportExcel(@RequestBody List<Integer> userIds){
        return this.userService.exportExcel(userIds);
    }
}
