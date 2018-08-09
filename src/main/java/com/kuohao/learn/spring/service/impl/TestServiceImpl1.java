package com.kuohao.learn.spring.service.impl;

import com.kuohao.learn.spring.service.TestService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午4:21
 * CopyRight: taobao
 * Descrption:  通过指定profile来指定运行时的bean
 */

@Service
@Profile("dev")
public class TestServiceImpl1 implements TestService {
    @Override
    public void sayHello() {
        System.out.println("say Hello dev");
    }
}
