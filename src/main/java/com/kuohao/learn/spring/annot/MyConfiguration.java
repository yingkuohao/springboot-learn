package com.kuohao.learn.spring.annot;

import com.kuohao.learn.spring.service.EmailService;
import com.kuohao.learn.spring.service.TestService;
import com.kuohao.learn.spring.service.impl.LinuxEmailService;
import com.kuohao.learn.spring.service.impl.TestServiceImpl1;
import com.kuohao.learn.spring.service.impl.WindowsEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午2:29
 * CopyRight: taobao
 * Descrption:
 */

@Configuration
public class MyConfiguration {

    @Bean(name = "emailService")
    @Conditional(WindowsCondition.class)
    public EmailService windowsTestService() {
        //当满足windowsCondition时,实例化    WindowsEmailService
        return new WindowsEmailService();
    }

    @Bean(name = "emailService")
    @Conditional(LinuxCondition.class)
    //当满足LinuxCondition时,实例化    WindowsEmailService
    public EmailService linuxTestService() {
        return new LinuxEmailService();
    }

}
