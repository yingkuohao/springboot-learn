package com.kuohao.learn.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午3:55
 * CopyRight: taobao
 * Descrption:
 */

//@Component
public class CommandLineTest implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(" commandLine Begin,");
        System.out.println("args="+args);
    }
}
