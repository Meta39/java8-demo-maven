package com.fu.mybatisplusdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fu.mybatisplusdemo.entity.User;
import com.fu.mybatisplusdemo.mapper.UserMapper;
import com.fu.mybatisplusdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据ID查询用户
     */
    @Override
    public User selectUserById(Integer id) {
        return this.userMapper.selectById(id);
    }

    /**
     * 新增用户
     */
    @Override
    public int insertUser(User user) {
        return this.userMapper.insert(user);
    }

    /**
     * 更新用户
     */
    @Override
    public int updateUser(User user) {
        return this.userMapper.updateById(user);
    }

    /**
     * 根据ID删除用户
     */
    @Override
    public int deleteUserById(Integer id) {
        return this.userMapper.deleteById(id);
    }

    /**
     * 分页查询用户
     */
    @Override
    public Page<User> selectUserPage(Long page, Long size) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        return this.userMapper.selectPage(Page.of(page, size), lqw);
    }

    /**
     * 查询用户
     * 列表
     */
    @Override
    public List<User> selectUserList(User user) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        return this.userMapper.selectList(lqw);
    }

    /**
     * 根据ID集合批量删除用户
     */
    @Override
    public int deleteUserByIds(List<Integer> ids) {
        return this.userMapper.deleteBatchIds(ids);
    }
}
