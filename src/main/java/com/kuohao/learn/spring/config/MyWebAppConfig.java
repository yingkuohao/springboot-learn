package com.kuohao.learn.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午5:50
 * CopyRight: taobao
 * Descrption:
 */
@Configuration
public class MyWebAppConfig implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(MyWebAppConfig.class);
    private RelaxedPropertyResolver propertyResolver;

    @Value("${spring.datasource.url}")
    private String myUrl;

    @Override
    public void setEnvironment(Environment env) {
        logger.info("----JAVA_HOME----"+env.getProperty("JAVA_HOME"));
        logger.info("----spring.datasource.url----"+myUrl);
        String str = env.getProperty("spring.datasource.url");
        logger.info(str);
        propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        String url = propertyResolver.getProperty("url");
        logger.info("---url---"+url);
    }
}
