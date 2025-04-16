package com.fu.mybatisplusdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.mybatisplusdemo.entity.User;
import com.fu.mybatisplusdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID查询用户
     *
     * @param id ID
     */
    @GetMapping("{id}")
    public User selectUserById(@PathVariable("id") Integer id) {
        return this.userService.selectUserById(id);
    }

    /**
     * 新增用户
     *
     * @param user 用户实体类
     */
    @PostMapping
    public int insertUser(@RequestBody @Valid User user) {
        return this.userService.insertUser(user);
    }

    /**
     * 更新用户
     *
     * @param user 用户实体类
     */
    @PutMapping
    public int updateUser(@RequestBody @Valid User user) {
        return this.userService.updateUser(user);
    }

    /**
     * 根据ID删除用户
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    public int deleteUser(@PathVariable Integer id) {
        return this.userService.deleteUserById(id);
    }

    /**
     * 查询用户 分页数据
     * PS：需要配置分页插件配置类
     */
    @GetMapping("paging")
    public Page<User> selectUserPage(@RequestParam(required = false, defaultValue = "1") Long page, @RequestParam(required = false, defaultValue = "10") Long size) {
        return this.userService.selectUserPage(page, size);
    }

    /**
     * 查询用户 列表
     */
    @GetMapping
    public List<User> selectUserList(User user) {
        return this.userService.selectUserList(user);
    }

    /**
     * 根据ID集合批量删除用户
     */
    @DeleteMapping
    public int deleteUsers(@RequestBody List<Integer> ids) {
        return this.userService.deleteUserByIds(ids);
    }
}
