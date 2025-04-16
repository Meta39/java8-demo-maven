package com.fu.velocitygeneratecodedemo.serviceimpl;

import com.fu.velocitygeneratecodedemo.entity.Tables;
import com.fu.velocitygeneratecodedemo.service.TablesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TablesServiceImpl implements TablesService {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Tables> selectTables() {
        String table = "spring_boot_demo";
        String sql = "select * from information_schema.tables where table_schema = ? ";
        List<Tables> tables = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tables.class), table);
        tables.forEach(System.out::println);
        return null;
    }

}
