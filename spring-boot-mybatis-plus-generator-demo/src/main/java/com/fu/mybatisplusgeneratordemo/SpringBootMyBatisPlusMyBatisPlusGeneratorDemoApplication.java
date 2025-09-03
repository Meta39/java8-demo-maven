package com.fu.mybatisplusgeneratordemo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.fu.mybatisplusgeneratordemo.config.DataBaseConfig;
import com.fu.mybatisplusgeneratordemo.config.MyBatisPlusGeneratorConfig;
import com.fu.mybatisplusgeneratordemo.config.OracleNumberTypeConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SpringBootMyBatisPlusMyBatisPlusGeneratorDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootMyBatisPlusMyBatisPlusGeneratorDemoApplication.class, args);
        MyBatisPlusGeneratorConfig myBatisPlusGeneratorConfig = context.getBean(MyBatisPlusGeneratorConfig.class);
        DataBaseConfig dataBaseConfig = context.getBean(DataBaseConfig.class);

        FastAutoGenerator.create(dataBaseConfig.getUrl(), dataBaseConfig.getUsername(), dataBaseConfig.getPassword())
                .dataSourceConfig(builder -> builder
                        //数据库字段映射处理
                        .typeConvertHandler(new OracleNumberTypeConverter())
                )
                .globalConfig(builder -> builder
                        .author(myBatisPlusGeneratorConfig.getAuthor())
                        .outputDir(myBatisPlusGeneratorConfig.getOutputDir())
                        .dateType(DateType.ONLY_DATE)
                        .commentDate(myBatisPlusGeneratorConfig.getCommentDate())
                )
                .packageConfig(builder -> builder
                        .parent(myBatisPlusGeneratorConfig.getParentPackage())
                        .entity(myBatisPlusGeneratorConfig.getEntityPackageName())
                        .mapper(myBatisPlusGeneratorConfig.getMapperPackageName())
                )
                .strategyConfig(builder -> builder
                        .enableSkipView() // 开启跳过视图（如果不跳过视图，Oracle 可能会报错）
                        .addInclude(getTables(myBatisPlusGeneratorConfig.getTables()))//默认全部表
                        //controller
                        .controllerBuilder()
                        .disable()//禁用controller 层生成
                        //service
                        .serviceBuilder()
                        .disableService()// 禁用 Service 层生成
                        .disableServiceImpl()//禁用 ServiceImpl 层生成
                        //mapper
                        .mapperBuilder()
                        .disableMapperXml()//禁用 mapper xml 文件生成
                        //entity
                        .entityBuilder()
                        .enableFileOverride()//覆盖已经生成的文件
                        .enableLombok() //使用 lombok
                        .naming(NamingStrategy.underline_to_camel) // 表字段转驼峰
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .disableSerialVersionUID()//禁用实现 Serializable 序列化接口
                        .idType(IdType.INPUT)
                        .build()
                )
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(List<String> tables) {
        return (CollectionUtils.isEmpty(tables) || tables.contains("all")) ? Collections.emptyList() : tables;
    }

}
