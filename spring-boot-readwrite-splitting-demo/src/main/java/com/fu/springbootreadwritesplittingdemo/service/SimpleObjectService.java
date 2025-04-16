package com.fu.springbootreadwritesplittingdemo.service;

import com.fu.springbootreadwritesplittingdemo.config.ReadOnly;
import com.fu.springbootreadwritesplittingdemo.entity.SimpleObject;
import com.fu.springbootreadwritesplittingdemo.mapper.SimpleObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 创建日期：2024-05-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleObjectService {
    private final SimpleObjectMapper simpleObjectMapper;

    @ReadOnly
    public SimpleObject selectById(Long id) {
        return simpleObjectMapper.selectById(id);
    }

}
