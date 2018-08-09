package com.kuohao.learn.spring.listener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午2:59
 * CopyRight: taobao
 * Descrption:
 */

@Component
public class MyPostProsessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
//        System.out.println("bean name =" + o.toString());
        return o;
    }
}
