package com.kuohao.learn.spring.controller;

import com.kuohao.learn.spring.config.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午2:41
 * CopyRight: taobao
 * Descrption:      RestController 注解,其实就是ResponseBody和controller注解的整合
 */

@org.springframework.web.bind.annotation.RestController
public class RestController {


    //定义一个全局的记录器，通过LoggerFactory获取
       private final static Logger logger = LoggerFactory.getLogger(RestController.class);
    @Autowired
    User user;

    @RequestMapping(value = "/sayUser", method = RequestMethod.GET)
    @ResponseBody
    public User sayUser(String name) {
        System.out.println("userName;" + user.getName());
        System.out.println("user;" +  user.toString());
        logger.info("test log " + user.getName());
        return  user;
    }
}
