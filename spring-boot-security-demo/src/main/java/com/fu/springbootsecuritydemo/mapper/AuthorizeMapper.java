package com.fu.springbootsecuritydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springbootsecuritydemo.entity.Authorize;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuthorizeMapper extends BaseMapper<Authorize> {

}
