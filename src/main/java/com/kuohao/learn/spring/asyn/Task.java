package com.kuohao.learn.spring.asyn;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/9/18
 * Time: ����5:59
 * CopyRight: taobao
 * Descrption:
 */

@Component
public class Task {
    public static Random random =new Random();

       public void doTaskOne() throws Exception {
           System.out.println("��ʼ������һ");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("�������һ����ʱ��" + (end - start) + "����");
       }

       public void doTaskTwo() throws Exception {
           System.out.println("��ʼ�������");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("������������ʱ��" + (end - start) + "����");
       }

       public void doTaskThree() throws Exception {
           System.out.println("��ʼ��������");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("�������������ʱ��" + (end - start) + "����");
       }

}
