package com.fu.springbootdemo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    /**
     * 根据ID查询用户
     */
    User selectUserById(Integer id);
    User getUserInfoByToken();


    /**
     * 新增用户
     */
    int insertUser(User user);

    /**
     * 更新用户
     */
    int updateUser(User user);

    int shareRoles(Integer userId,List<Integer> roleIds);

    int isBanUser(Integer id,Integer isBan);

    /**
     * 根据ID删除用户
     */
    int deleteUserById(Integer id);

    /**
     * 分页查询用户
     */
    Page<User> selectUserPage(Long page, Long size,String username);

    /**
     * 根据ID集合批量删除用户
     */
    int deleteUserByIds(List<Integer> ids);

    boolean importExcel(MultipartFile file);

    String exportExcel(List<Integer> userIds);

}
