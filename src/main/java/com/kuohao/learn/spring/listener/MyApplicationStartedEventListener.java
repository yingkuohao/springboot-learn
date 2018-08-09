package com.kuohao.learn.spring.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/4/28
 * Time: 下午2:34
 * CopyRight: taobao
 * Descrption:
 * 该事件可以获得SpringApplication对象,可做一些执行前设置
 */

public class MyApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private Logger logger = LoggerFactory.getLogger(MyApplicationStartedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        SpringApplication app =applicationStartedEvent.getSpringApplication();
        app.setShowBanner(false);        // 不显示banner信息
        System.out.println("--MyApplicationStartedEventListener execute!--");
        logger.info("==MyApplicationStartedEventListener==");
    }
}
