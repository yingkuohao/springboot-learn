package com.kuohao.learn.spring.asyn;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/9/18
 * Time: 下午5:59
 * CopyRight: taobao
 * Descrption:
 */

@Component
public class Task {
    public static Random random =new Random();

       public void doTaskOne() throws Exception {
           System.out.println("开始做任务一");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
       }

       public void doTaskTwo() throws Exception {
           System.out.println("开始做任务二");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
       }

       public void doTaskThree() throws Exception {
           System.out.println("开始做任务三");
           long start = System.currentTimeMillis();
           Thread.sleep(random.nextInt(10000));
           long end = System.currentTimeMillis();
           System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
       }

}
