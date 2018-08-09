package com.kuohao.learn.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/3
 * Time: 下午2:18
 * CopyRight: taobao
 * Descrption:
 */
@Component
//@ConfigurationProperties(prefix = "systemTest",locations = "/Users/chengjing/bin/systemTest.yml")
@ConfigurationProperties(prefix = "systemTest",locations = "classpath:config/systemTest.yml")
public class SysTemVO {//貌似不能取绝对路径    ,测试的取不到
    private String name;
    private String url;
    private String port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
