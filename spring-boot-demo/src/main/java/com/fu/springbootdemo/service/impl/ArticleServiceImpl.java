package com.fu.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fu.springbootdemo.entity.Article;
import com.fu.springbootdemo.mapper.ArticleMapper;
import com.fu.springbootdemo.service.ArticleService;
import com.fu.springbootdemo.util.CurrentLoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文章 业务层接口实现
 *
 * @author Meta39
 * @since 2023-04-15 23:06:07
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;

    /**
     * 根据ID查询文章
     */
    @Override
    public Article selectArticleById(Long id) {
        return this.articleMapper.selectById(id);
    }

    /**
     * 新增文章
     */
    @Override
    public int insertArticle(Article article) {
        article.setUserId(CurrentLoginUserUtil.getUserId());
        return this.articleMapper.insert(article);
    }

    /**
     * 更新文章
     */
    @Override
    public int updateArticle(Article article) {
        return this.articleMapper.updateById(article);
    }

    /**
     * 根据ID删除文章
     */
    @Override
    public int deleteArticleById(Long id) {
        return this.articleMapper.deleteById(id);
    }

    /**
     * 分页查询文章
     */
    @Override
    public Page<Article> selectArticlePage(Long page, Long size,String title) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getUserId,CurrentLoginUserUtil.getUserId())
                .orderByDesc(Article::getCreateTime);
        if (StringUtils.hasText(title)){
            lqw.like(Article::getTitle,title);
        }
        return this.articleMapper.selectPage(Page.of(page, size), lqw);
    }

    /**
     * 查询文章
     * 列表
     */
    @Override
    public List<Article> selectArticleList(Article article) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        return this.articleMapper.selectList(lqw);
    }

    /**
     * 根据ID集合批量删除文章
     */
    @Override
    public int deleteArticleByIds(List<Long> ids) {
        return this.articleMapper.deleteBatchIds(ids);
    }
}
