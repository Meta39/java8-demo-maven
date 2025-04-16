package com.fu.springbootdemo.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fu.springbootdemo.easyexcel.UserReadListener;
import com.fu.springbootdemo.entity.User;
import com.fu.springbootdemo.entity.UserRole;
import com.fu.springbootdemo.global.GlobalVariable;
import com.fu.springbootdemo.mapper.RoleMapper;
import com.fu.springbootdemo.mapper.UserMapper;
import com.fu.springbootdemo.mapper.UserRoleMapper;
import com.fu.springbootdemo.service.UserService;
import com.fu.springbootdemo.util.CurrentLoginUserUtil;
import com.fu.springbootdemo.util.RSAUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据ID查询用户
     */
    @Override
    public User selectUserById(Integer id) {
        return this.userMapper.selectById(id);
    }

    @Override
    public User getUserInfoByToken() {
        return this.userMapper.selectById(CurrentLoginUserUtil.getUserId());
    }

    /**
     * 新增用户
     */
    @Override
    public int insertUser(User user) {
        if (!StringUtils.hasLength(user.getPwd())) {
            new RuntimeException("密码不能为空");
        }
        user.setPwd(RSAUtil.encrypt(GlobalVariable.RSA_TOKEN_PUBLIC_KEY,user.getPwd()));
        return this.userMapper.insert(user);
    }

    /**
     * 更新用户
     */
    @Override
    public int updateUser(User user) {
        if (StringUtils.hasLength(user.getPwd())) {
            new RuntimeException("更新用户时不允许修改密码");
        }
        return this.userMapper.updateById(user);
    }

    @Override
    public int shareRoles(Integer userId,List<Integer> roleIds) {
        if (userId == 1){
            new RuntimeException("不允许给超级用户分配其它角色");
        }
        //省事做法：不管之前有没有角色，都把之前的该用户的角色删光。再从新插入角色。
        this.userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,userId));
        if (!roleIds.isEmpty()){
            roleIds.forEach(roleId ->{
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                this.userRoleMapper.insert(userRole);
            });
        }
        return 1;
    }

    @Override
    public int isBanUser(Integer id, Integer isBan) {
        return this.userMapper.update(null,new LambdaUpdateWrapper<User>()
                .eq(User::getId,id)
                .set(User::getIsBan,isBan));
    }

    /**
     * 根据ID删除用户
     */
    @Override
    public int deleteUserById(Integer id) {
        if (id == 1) {
            new RuntimeException("不允许删除超级用户");
        }
        return this.userMapper.deleteById(id);
    }

    /**
     * 分页查询用户
     */
    @Override
    public Page<User> selectUserPage(Long page, Long size,String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)){
            lqw.like(User::getUsername,username);
        }
        Page<User> userPage = this.userMapper.selectPage(Page.of(page, size), lqw);
        userPage.getRecords().forEach(user -> {
            //获取用户列表对应的角色列表集合
            List<Integer> roleIds = this.userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId())).stream()
                    .map(UserRole::getRoleId).collect(Collectors.toList());
            if (!roleIds.isEmpty()){
                user.setRoles(new HashSet<>(this.roleMapper.selectBatchIds(roleIds)));
            }
        });
        return userPage;
    }

    /**
     * 根据ID集合批量删除用户
     */
    @Override
    public int deleteUserByIds(List<Integer> ids) {
        if (ids != null && !ids.isEmpty() && ids.stream().anyMatch(id -> id == 1)) {
            new RuntimeException("不允许删除超级用户");
        }
        return this.userMapper.deleteBatchIds(ids);
    }

    @Override
    @SneakyThrows
    public boolean importExcel(MultipartFile file) {
        EasyExcel.read(file.getInputStream(),User.class,new UserReadListener(this.userMapper)).sheet().doRead();
        return true;
    }

    @Override
    public String exportExcel(List<Integer> userIds) {
        String fileName = "导出用户列表.xlsx";
        EasyExcel.write(fileName, User.class)
                .sheet()
                .doWrite(() -> {
                    if (userIds.isEmpty()){//全部数据
                        return this.userMapper.selectList(null);
                    }else {//选中的数据
                        return this.userMapper.selectBatchIds(userIds);
                    }
                });
        return fileName;
    }

}
