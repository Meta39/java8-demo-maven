package com.fu.springbootsecuritydemo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fu.springbootsecuritydemo.entity.Authorize;

import java.util.List;

public interface AuthorizeService extends IService<Authorize> {
    /**
     * 根据ID查询权限
     */
    Authorize selectAuthorizeById(Integer id);

    /**
     * 新增权限
     */
    int insertAuthorize(Authorize authorize);

    /**
     * 更新权限
     */
    int updateAuthorize(Authorize authorize);

    /**
     * 根据ID删除权限
     */
    int deleteAuthorizeById(Integer id);

    /**
     * 分页查询权限
     */
    Page<Authorize> selectAuthorizePage(Long page, Long size);

    /**
     * 查询权限
     * 列表
     */
    List<Authorize> selectAuthorizeList(Authorize authorize);

    /**
     * 根据ID集合批量删除权限
     */
    int deleteAuthorizeByIds(List<Integer> ids);
}
