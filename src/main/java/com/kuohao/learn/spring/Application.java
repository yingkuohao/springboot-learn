package com.kuohao.learn.spring;

import com.kuohao.learn.spring.listener.MyApplicationEnvironmentPreparedEventListener;
import com.kuohao.learn.spring.listener.MyApplicationStartedEventListener;
import com.kuohao.learn.spring.servlet.MyServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/4/28
 * Time: 下午2:17
 * CopyRight: taobao
 * Descrption:    主启动程序
 */
@SpringBootApplication
@EnableConfigurationProperties
//@ServletComponentScan   //扫描自定义servlet的注解
public class Application {
    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        SpringApplication app = new SpringApplication(Application.class);
        //添加自定义listner
        app.addListeners(new MyApplicationStartedEventListener());
        app.addListeners(new MyApplicationEnvironmentPreparedEventListener());
//        app.setBanner(new MyBanner());
        app.run(args);


    }

    //http://localhost:8080/xs/myServlet
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new MyServlet(), "/xs/*");// ServletName默认值为首字母小写，即myServlet
    }
}
