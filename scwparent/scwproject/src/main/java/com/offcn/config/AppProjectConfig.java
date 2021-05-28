package com.offcn.config;


import com.offcn.user.util.OssTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//将application配置文件中把oss中的配置信息给读出来存放到官方给的这个OssTemplate对象中并注入到ioc容器
@Configuration
public class AppProjectConfig {
    @ConfigurationProperties(prefix="oss") //加载配置文件中的oss属性
    @Bean
    public OssTemplate ossTemplate(){
        return new OssTemplate();
    }
}