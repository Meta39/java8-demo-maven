package com.fu.springbootarthasdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootArthasDemoApplication {

    /*
        注意事项：build image 时需要开启代理，否则会打包失败。
        1.启动 docker 镜像：docker run -d -p 100:100 --name spring-boot-arthas-demo spring-boot-arthas-demo:latest
        2.查看是否启动成功：docker ps --filter "name=spring-boot-arthas-demo"
        3.查看是否启动成功：http://localhost:100/hello
        4.诊断 Docker 里的 Java 进程【出现对话框填1敲回车】：docker exec -it spring-boot-arthas-demo /bin/bash -c "wget https://arthas.aliyun.com/arthas-boot.jar && java -jar arthas-boot.jar"
        5.通过 watch 命令来查看 com.fu.springbootarthasdemo.controller.TestController#hello 函数的返回值：
            watch com.fu.springbootarthasdemo.controller.TestController hello returnObj
            打开浏览器输入：http://localhost:100/hello
            控制台查看是否有输出：ts=2026-01-19 08:52:27.746; [cost=2.475554ms] result=@String[hello]
        6.退出 arthas：quit 或者 exit，如果想完全退出 arthas，可以执行stop命令。
        7.删除镜像：docker rm -f spring-boot-arthas-demo
        8.文档地址：https://arthas.aliyun.com/doc
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArthasDemoApplication.class, args);
    }

}
