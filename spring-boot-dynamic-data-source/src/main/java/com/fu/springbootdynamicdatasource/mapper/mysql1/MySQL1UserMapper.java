package com.fu.springbootdynamicdatasource.mapper.mysql1;

import com.fu.springbootdynamicdatasource.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 数据源1
 */
public interface MySQL1UserMapper {
    User selectById(@Param("id") Integer id);

    int insert(User user);
}
