package com.fu.mybatisplusdemo.controller;

import com.fu.mybatisplusdemo.entity.EntityA;
import com.fu.mybatisplusdemo.mapper.TreeLevelResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MyBatis 嵌套3层以上通过编写自定义的 resultMap 处理复杂结果映射
 */
@RestController
@RequestMapping("treeLevelResult")
@RequiredArgsConstructor
public class TreeLevelResultController {
    private final TreeLevelResultMapper treeLevelResultMapper;

    @GetMapping
    public List<EntityA> treeLevelResult() {
        return treeLevelResultMapper.findTreeLevelResult();
    }

}
