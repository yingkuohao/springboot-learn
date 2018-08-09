package com.kuohao.learn.spring.asyn;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/9/18
 * Time: ����5:59
 * CopyRight: taobao
 * Descrption:
 */

@Component
public class AsyncTask {
    public static Random random = new Random();

    @Async
    public Future<String> doTaskOne() throws Exception {
        System.out.println("��ʼ������һ");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("�������һ����ʱ��" + (end - start) + "����");
        return new AsyncResult<String>("����һ���");
    }

    @Async
    public Future<String> doTaskTwo() throws Exception {
        System.out.println("��ʼ�������");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("������������ʱ��" + (end - start) + "����");
        return new AsyncResult<String>("��������");
    }

    @Async
    public Future<String> doTaskThree() throws Exception {
        System.out.println("��ʼ��������");
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("�������������ʱ��" + (end - start) + "����");
        return new AsyncResult<String>("���������");
    }

}
