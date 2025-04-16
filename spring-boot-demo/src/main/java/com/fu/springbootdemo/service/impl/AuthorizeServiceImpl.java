package com.fu.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.entity.Authorize;
import com.fu.springbootdemo.mapper.AuthorizeMapper;
import com.fu.springbootdemo.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorizeServiceImpl implements AuthorizeService {
    private final AuthorizeMapper authorizeMapper;

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

    @Override
    public List<Authorize> selectAuthorizeTree() {
        List<Authorize> authorizes = this.authorizeMapper.selectList(null);
        return authorizes.stream().filter(authorize -> authorize.getPId() == 0).peek(authorize -> authorize.setChildAuthorize(authorizeTree(authorize, authorizes))).collect(Collectors.toList());
    }


    //============================内部方法=======================================

    /**
     * 递归权限树，展示全部权限
     */
    private static List<Authorize> authorizeTree(Authorize authorize, List<Authorize> authorizes) {
        return authorizes.stream()
                .filter(a -> a.getPId().equals(authorize.getId()))
                .peek(a -> a.setChildAuthorize(authorizeTree(a, authorizes)))
                .collect(Collectors.toList());
    }

}
