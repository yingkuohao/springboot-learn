package com.kuohao.learn.spring;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午3:31
 * CopyRight: taobao
 * Descrption:自定义启动展示Banner
 * z
 */

public class MyBanner implements Banner {

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println("Lottery starting!");
    }
}
