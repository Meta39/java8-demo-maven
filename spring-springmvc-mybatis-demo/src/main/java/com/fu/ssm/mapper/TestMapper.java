package com.fu.ssm.mapper;

import com.fu.ssm.dto.TestDTO;
import com.fu.ssm.entity.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {
    Test selectByTestId(Integer id);

    List<Test> selectTestPage(TestDTO testDTO);
}
