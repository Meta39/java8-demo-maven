package com.fu.springbootsecuritydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fu.springbootsecuritydemo.entity.Authorize;
import com.fu.springbootsecuritydemo.mapper.AuthorizeMapper;
import com.fu.springbootsecuritydemo.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizeServiceImpl extends ServiceImpl<AuthorizeMapper, Authorize> implements AuthorizeService {
    @Autowired
    private AuthorizeMapper authorizeMapper;

    /**
     * 根据ID查询权限
     */
    @Override
    public Authorize selectAuthorizeById(Integer id) {
        return this.authorizeMapper.selectById(id);
    }

    /**
     * 新增权限
     */
    @Override
    public int insertAuthorize(Authorize authorize) {
        return this.authorizeMapper.insert(authorize);
    }

    /**
     * 更新权限
     */
    @Override
    public int updateAuthorize(Authorize authorize) {
        if (authorize.getId() == 1){
            throw new RuntimeException("不允许修改id为1的内容");
        }
        return this.authorizeMapper.updateById(authorize);
    }

    /**
     * 根据ID删除权限
     */
    @Override
    public int deleteAuthorizeById(Integer id) {
        if (id == 1){
            throw new RuntimeException("不允许删除id为1的内容");
        }
        return this.authorizeMapper.deleteById(id);
    }

    /**
     * 分页查询权限
     */
    @Override
    public Page<Authorize> selectAuthorizePage(Long page, Long size) {
        LambdaQueryWrapper<Authorize> lqw = new LambdaQueryWrapper<>();
        return this.authorizeMapper.selectPage(Page.of(page, size), lqw);
    }

    /**
     * 查询权限
     * 列表
     */
    @Override
    public List<Authorize> selectAuthorizeList(Authorize authorize) {
        LambdaQueryWrapper<Authorize> lqw = new LambdaQueryWrapper<>();
        return this.authorizeMapper.selectList(lqw);
    }

    /**
     * 根据ID集合批量删除权限
     */
    @Override
    public int deleteAuthorizeByIds(List<Integer> ids) {
        if (ids.stream().anyMatch(id -> id == 1)){
            throw new RuntimeException("不允许删除id为1的内容");
        }
        return this.authorizeMapper.deleteBatchIds(ids);
    }
}
