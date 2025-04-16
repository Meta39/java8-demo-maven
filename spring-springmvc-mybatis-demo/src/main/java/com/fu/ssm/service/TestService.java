package com.fu.ssm.service;

import com.fu.ssm.dto.TestDTO;
import com.fu.ssm.entity.Test;
import com.github.pagehelper.PageSerializable;

public interface TestService {

    Test selectByTestId(Integer id);

    PageSerializable<Test> selectTestPage(TestDTO testDTO);
}
