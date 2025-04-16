package com.fu.ssm.service.impl;

import com.fu.ssm.dto.TestDTO;
import com.fu.ssm.entity.Test;
import com.fu.ssm.mapper.TestMapper;
import com.fu.ssm.service.TestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestMapper testMapper;

    @Override
    public Test selectByTestId(Integer id) {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        return testMapper.selectByTestId(id);
    }

    @Override
    public PageSerializable<Test> selectTestPage(TestDTO testDTO) {
        //PageHelper底层使用ThreadLocal防止并发造成数据问题，因此用完要关闭ThreadLocal。
        PageHelper.startPage(testDTO).close();
        //PageInfo会返回更为详细的内容，PageSerializable只返回list和total。
//        log.info("{}", PageInfo.of(testMapper.selectTestPage(testDTO)));
        return PageSerializable.of(testMapper.selectTestPage(testDTO));
    }

}
