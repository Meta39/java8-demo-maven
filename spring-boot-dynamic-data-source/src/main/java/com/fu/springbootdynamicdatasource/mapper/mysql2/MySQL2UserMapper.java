package com.fu.springbootdynamicdatasource.mapper.mysql2;

import com.fu.springbootdynamicdatasource.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 数据源2
 */
public interface MySQL2UserMapper {
    User selectById(@Param("id") Integer id);

    int insert(User user);
}
