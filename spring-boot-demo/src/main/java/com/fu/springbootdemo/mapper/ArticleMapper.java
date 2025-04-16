package com.fu.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springbootdemo.entity.Article;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文章 mapper映射
 *
 * @author Meta39
 * @since 2023-04-15 23:06:07
 */
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

}
