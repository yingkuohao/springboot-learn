package com.kuohao.learn.spring.annot;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午2:28
 * CopyRight: taobao
 * Descrption:
 */

public class WindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //当环境变量:my.os.name==windows时返回true
        return context.getEnvironment().getProperty("my.os.name").contains("windows");
    }
}
