package com.fu.mybatisplusdemo;

import com.fu.mybatisplusdemo.mapper.SimpleObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SimpleObjectMapperTests {
    @Autowired
    private SimpleObjectMapper simpleObjectMapper;

    @Test
    public void test() {
        simpleObjectMapper.selectById(1);
    }

}
