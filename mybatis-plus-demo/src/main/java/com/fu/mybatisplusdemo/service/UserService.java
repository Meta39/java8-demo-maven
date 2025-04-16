package com.fu.mybatisplusdemo.service;

import com.fu.mybatisplusdemo.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 根据ID查询用户
     */
    User selectUserById(Integer id);

    /**
     * 新增用户
     */
    int insertUser(User user);

    /**
     * 更新用户
     */
    int updateUser(User user);

    /**
     * 根据ID删除用户
     */
    int deleteUserById(Integer id);

    /**
     * 分页查询用户
     */
    Page<User> selectUserPage(Long page, Long size);

    /**
     * 查询用户
     * 列表
     */
    List<User> selectUserList(User user);

    /**
     * 根据ID集合批量删除用户
     */
    int deleteUserByIds(List<Integer> ids);
}
