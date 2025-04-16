package com.fu.ssm.controller;

import com.fu.ssm.dto.TestDTO;
import com.fu.ssm.entity.Test;
import com.fu.ssm.service.TestService;
import com.github.pagehelper.PageSerializable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    /**
     * 通过test表的id获取test信息
     * @param id 主键
     */
    @RequestMapping(value = "selectTestById", method = RequestMethod.POST)
    @ResponseBody
    public Test selectByTestId(@RequestBody Integer id) {
        return testService.selectByTestId(id);
    }

    /**
     * 分页查询test表记录
     * @param testDTO test数据传输对象
     */
    @RequestMapping(value = "selectTestPage", method = RequestMethod.POST)
    @ResponseBody
    public PageSerializable<Test> selectTestPage(@RequestBody TestDTO testDTO) {
        return testService.selectTestPage(testDTO);
    }

}