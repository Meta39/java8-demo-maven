package com.fu.springbootdemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.entity.Article;
import com.fu.springbootdemo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 文章 控制层
 *
 * @author Meta39
 * @since 2023-04-15 23:06:07
 */
@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 根据ID查询文章
     *
     * @param id ID
     */
    @GetMapping("{id}")
    public Article selectArticleById(@PathVariable("id") Long id) {
        return this.articleService.selectArticleById(id);
    }

    /**
     * 新增文章
     *
     * @param article 文章实体类
     */
    @PostMapping
    public Integer insertArticle(@RequestBody @Valid Article article) {
        return this.articleService.insertArticle(article);
    }

    /**
     * 更新文章
     *
     * @param article 文章实体类
     */
    @PutMapping
    public Integer updateArticle(@RequestBody @Valid Article article) {
        return this.articleService.updateArticle(article);
    }

    /**
     * 根据ID删除文章
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    public Integer deleteArticle(@PathVariable Long id) {
        return this.articleService.deleteArticleById(id);
    }

    /**
     * 查询文章 分页数据
     */
    @GetMapping("page")
    public Page<Article> selectArticlePage(@RequestParam(required = false, defaultValue = "1") Long page,
                                           @RequestParam(required = false, defaultValue = "10") Long size,
                                           @RequestParam(required = false) String title) {
        return this.articleService.selectArticlePage(page, size,title);
    }

    /**
     * 根据ID集合批量删除文章
     */
    @DeleteMapping("deleteBatch")
    public Integer deleteArticles(@RequestBody List<Long> ids) {
        return this.articleService.deleteArticleByIds(ids);
    }
}
