package com.kuohao.learn.spring.http;

import com.kuohao.learn.spring.http.consts.APPConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.zip.CRC32;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/8/8
 * Time: 上午10:16
 * CopyRight: taobao
 * Descrption:
 */

public class AccessUtil {
    private final static Logger logger = LoggerFactory.getLogger(AccessUtil.class);
    private static long DEFAULT_EXPIRETIME = 60 * 1;      //接口token默认有效值3分钟
    private static long DEFAULT_EXPIRETIME_OFFSET = 20;      //接口token和lasttoken的衔接有效期10秒,即10秒内两个token都能访问接口


    static volatile boolean intterrupt = true;

    public static boolean refreshToken(String appKey) {
        intterrupt = true;
        System.out.println("###access refresh");
        logger.info("---access refresh--");
        if (validAppKey(appKey)) {
            APPInfo appInfo = APPConsts.appInfoMap.get(appKey);
            //生成新token和时间戳,更新内存
            String appSecret = AESTool.getRandomId();
            String token = AESTool.getRandomId();
            long timestamp = Instant.now().toEpochMilli();
            appInfo.setLastAppSecret(appInfo.getAppSecret());
            appInfo.setAppSecret(appSecret);
            appInfo.setLastToken(appInfo.getToken());//把当前的token转移到lasttoken位置,
            appInfo.setToken(token);
            appInfo.setTimstamp(timestamp);
            //yingkhtodo:desc:更新数据库

            createCountThread();  //yingkhtodo:计数使用,方便测试token和lasttoken的失效,线上删掉
            return true;
        }
        return false;
    }

    private static void createCountThread() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    intterrupt = false;
                    for (int i = 0; i < DEFAULT_EXPIRETIME; i++) {
                        System.out.println("---time=" + i);
                        if (intterrupt) {
                            break;
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }

    public static boolean validAppKey(String appKey) {
        //判断appkey是否在内存中存在
        if (StringUtils.isEmpty(appKey) || !APPConsts.appInfoMap.containsKey(appKey)) {
            logger.error("appkey  is empty!");
            return false;
        }
        return true;
    }

    public static String checkAppToken(String appKey, String token) {
        if (validAppToken(appKey, token)) {
            //如果token等于lasttoken,要返回lastSecret,保证token和secret统一
            APPInfo appInfo = APPConsts.appInfoMap.get(appKey);
            if (token.equals(appInfo.getLastToken())) {
                return appInfo.getLastAppSecret();
            }
            return appInfo.getAppSecret();
        }
        return null;
    }


    public static boolean validAppToken(String appKey, String token) {
        if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(token)) {
            logger.error("appkey or token is empty!");
            return false;
        }

        if (APPConsts.appInfoMap.containsKey(appKey)) {
            APPInfo appInfo = APPConsts.appInfoMap.get(appKey);
            //  解析appinfo中的时间戳加上1天后是否大于当前时间,如果大于则超期
            System.out.println("appInfo=" + appInfo.toString() + ",time=" + Instant.ofEpochMilli(appInfo.getTimstamp()).toString());
            long timstamp = appInfo.getTimstamp();
            if (token.equals(appInfo.getToken())) {
                //如果token有效,校验时间
                return isInTimeLimit(timstamp, DEFAULT_EXPIRETIME);
            } else {
                //如果token无效,校验与lasttoken是否相等
                String lastToken = appInfo.getLastToken();
                if (StringUtils.isEmpty(lastToken)) {
                    return false;
                } else {
                    if (lastToken.equals(token)) {
                        logger.info("---compare with lasttoken!--");
                        //如果token和lasttoken相等,则校验时间戳是否在临界期内
                        return isInTimeLimit(timstamp, DEFAULT_EXPIRETIME_OFFSET);
                    }
                }
            }

        }
        return false;
    }

    private static boolean isInTimeLimit(long timstamp, long expireTime) {
        if (timstamp == 0) {
            //如果是第一次请求,timstampe是空的
            return true;
        }
        //校验记录中的时间戳加上偏移量后是否在当前时间之后,如果是,则未过期.
        return Instant.ofEpochMilli(timstamp).plusSeconds(expireTime).isAfter(Instant.now());
    }

    public static long encrptByCRC(String text) {
        CRC32 crc32 = new CRC32();
        crc32.update(text.getBytes());

        return crc32.getValue();
    }


    public static void main(String[] args) {
        System.out.println(encrptByCRC("hello world"));

        AppService appService = new AppService();
        appService.init();
        //第一次获取token
        String appkey = APPConsts.APPKEY;
        APPInfo appInfo = APPConsts.appInfoMap.get(appkey);
        refreshToken(appkey);
        System.out.println("中文appInfo=" + appInfo.toString() + ",time=" + Instant.ofEpochMilli(appInfo.getTimstamp()).toString());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //第二次
        refreshToken(appkey);
        System.out.println("appInfo=" + appInfo.toString() + ",time=" + Instant.ofEpochMilli(appInfo.getTimstamp()).toString());
        //校验token是否有效
        boolean tokenValid = validAppToken(appkey, appInfo.getToken());
        boolean lastTokenValid = validAppToken(appkey, appInfo.getLastToken());
        System.out.println("tokenValid=" + tokenValid + ",lastTokenValid=" + lastTokenValid);
        System.out.println("appInfo=" + appInfo.toString());
    }

}
