package com.kuohao.learn.spring.service.impl;

import com.kuohao.learn.spring.service.EmailService;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午2:31
 * CopyRight: taobao
 * Descrption:
 */

public class WindowsEmailService implements EmailService {
    @Override
    public void sendEmail() {
        System.out.println("windows send Email");
    }
}
