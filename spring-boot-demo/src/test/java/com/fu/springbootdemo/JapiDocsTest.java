package com.fu.springbootdemo;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import org.junit.jupiter.api.Test;

public class JapiDocsTest {

    @Test
    public void test(){
        DocsConfig config = new DocsConfig();
        String projectName = "spring-boot-demo离线接口文档";
        config.setProjectPath("D:\\workspace\\demo-maven\\spring-boot-demo\\src\\main\\java\\com\\fu\\springbootdemo"); // 项目根目录
        config.setProjectName(projectName); // 项目名称
        config.setApiVersion(projectName);       // 声明该API的版本
        config.setDocsPath("d:/"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }

}
