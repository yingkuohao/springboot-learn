package com.kuohao.learn.spring.http.consts;

import com.kuohao.learn.spring.http.APPInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/7/28
 * Time: 下午2:18
 * CopyRight: taobao
 * Descrption:
 */

public class APPConsts {
    public static final String APPKEY = "shunfeng1234";
    public static final String APPSECRET = "123456";
    public static final String APPTOKEN = "123456";

    public static final String APP_SECRET_KEY = "appsecret";
    public static final String APP_TOKE_KEY = "apptoken";

    public static final String NOTIFY_URL = "http://localhost:8080/client/notify/token";

    public static Map<String, APPInfo> appInfoMap = new HashMap<String, APPInfo>();

}
