package com.fu.mybatisplusdemo;

import com.fu.mybatisplusdemo.mapper.OracleEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class OracleEntityMapperTests {
    @Autowired
    private OracleEntityMapper oracleEntityMapper;

    @Test
    public void test() {
        log.info("{}", this.oracleEntityMapper.selectById(1));
    }

}
