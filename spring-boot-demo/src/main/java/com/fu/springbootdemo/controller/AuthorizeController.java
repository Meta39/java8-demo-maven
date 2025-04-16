package com.fu.springbootdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.annotation.PreAuthorize;
import com.fu.springbootdemo.entity.Authorize;
import com.fu.springbootdemo.service.AuthorizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 权限
 */
@RestController
@RequestMapping("authorize")
@RequiredArgsConstructor
public class AuthorizeController {
    private final AuthorizeService authorizeService;

    /**
     * 根据ID查询权限
     *
     * @param id ID
     */
    @PreAuthorize("authorize:select")
    @GetMapping("{id}")
    public Authorize selectAuthorizeById(@PathVariable("id") Integer id) {
        return this.authorizeService.selectAuthorizeById(id);
    }

    /**
     * 新增权限
     *
     * @param authorize 权限实体类
     */
    @PreAuthorize("authorize:insert")
    @PostMapping
    public Integer insertAuthorize(@RequestBody @Valid Authorize authorize) {
        return this.authorizeService.insertAuthorize(authorize);
    }

    /**
     * 更新权限
     *
     * @param authorize 权限实体类
     */
    @PreAuthorize("authorize:update")
    @PutMapping
    public Integer updateAuthorize(@RequestBody @Valid Authorize authorize) {
        return this.authorizeService.updateAuthorize(authorize);
    }

    /**
     * 根据ID删除权限
     *
     * @param id ID
     */
    @PreAuthorize("authorize:delete")
    @DeleteMapping("{id}")
    public Integer deleteAuthorize(@PathVariable Integer id) {
        return this.authorizeService.deleteAuthorizeById(id);
    }

    /**
     * 查询权限 分页数据
     */
    @PreAuthorize("authorize:select")
    @GetMapping("page")
    public Page<Authorize> selectAuthorizePage(@RequestParam(required = false, defaultValue = "1") Long page, @RequestParam(required = false, defaultValue = "10") Long size) {
        return this.authorizeService.selectAuthorizePage(page, size);
    }

    /**
     * 查询权限 列表
     */
    @PreAuthorize("authorize:select")
    @GetMapping
    public List<Authorize> selectAuthorizeList(Authorize authorize) {
        return this.authorizeService.selectAuthorizeList(authorize);
    }

    /**
     * 根据ID集合批量删除权限
     */
    @PreAuthorize("authorize:delete")
    @DeleteMapping
    public Integer deleteAuthorizes(@RequestBody List<Integer> ids) {
        return this.authorizeService.deleteAuthorizeByIds(ids);
    }

    /**
     * 查询权限树
     */
    @PreAuthorize("authorize:select")
    @GetMapping("tree")
    public List<Authorize> selectAuthorizeTree() {
        return this.authorizeService.selectAuthorizeTree();
    }
}
