package com.fu.xxljobdemo.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties("xxl-job.executor")
public class XxlJobConfig {
    /**
     * xxl-job的admin地址
     */
    private String adminAddresses = "http://127.0.0.1:9998/xxl_job";
    /**
     * 当前应用名称
     */
    private String appName = "noSetXxlJobAppName";
    /**
     * 当前应用连接到admin的端口号
     */
    private Integer port = 9999;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setPort(port);
        return xxlJobSpringExecutor;
    }

    //--------------------------------set/get-----------------------------------------

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XxlJobConfig that = (XxlJobConfig) o;
        return Objects.equals(adminAddresses, that.adminAddresses) && Objects.equals(appName, that.appName) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminAddresses, appName, port);
    }

    @Override
    public String toString() {
        return "XxlJobConfig{" +
                "adminAddresses='" + adminAddresses + '\'' +
                ", appName='" + appName + '\'' +
                ", port=" + port +
                '}';
    }
}
