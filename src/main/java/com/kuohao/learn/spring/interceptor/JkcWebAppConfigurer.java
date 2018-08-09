package com.kuohao.learn.spring.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/11/2
 * Time: 下午2:39
 * CopyRight: taobao
 * Descrption:
 */
@Configuration
public class JkcWebAppConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(authInterceptor).addPathPatterns("/jkc/sf/*");
        super.addInterceptors(registry);
    }
}
