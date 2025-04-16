package com.fu.springbootdemo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.entity.Article;

import java.util.List;

/**
 * 文章 业务层接口
 *
 * @author Meta39
 * @since 2023-04-15 23:06:07
 */
public interface ArticleService {
    /**
     * 根据ID查询文章
     */
    Article selectArticleById(Long id);

    /**
     * 新增文章
     */
    int insertArticle(Article article);

    /**
     * 更新文章
     */
    int updateArticle(Article article);

    /**
     * 根据ID删除文章
     */
    int deleteArticleById(Long id);

    /**
     * 分页查询文章
     */
    Page<Article> selectArticlePage(Long page, Long size,String title);

    /**
     * 查询文章
     * 列表
     */
    List<Article> selectArticleList(Article article);

    /**
     * 根据ID集合批量删除文章
     */
    int deleteArticleByIds(List<Long> ids);
}
