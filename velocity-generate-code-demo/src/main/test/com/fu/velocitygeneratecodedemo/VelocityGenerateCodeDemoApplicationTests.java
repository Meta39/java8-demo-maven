package com.fu.velocitygeneratecodedemo;

import com.fu.velocitygeneratecodedemo.service.TablesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VelocityGenerateCodeDemoApplicationTests {
    @Autowired
    private TablesService tablesService;

    @Test
    void test(){
        tablesService.selectTables();
    }

}
