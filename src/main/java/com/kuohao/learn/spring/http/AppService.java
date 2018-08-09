package com.kuohao.learn.spring.http;

import com.kuohao.learn.spring.http.consts.APPConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/8/8
 * Time: 上午10:18
 * CopyRight: taobao
 * Descrption:
 */
@Component("appService")
public class AppService {

    private final static Logger logger = LoggerFactory.getLogger(AppService.class);

    @PostConstruct
    public void init() {
        //yingkhtodo:desc:读数据库,初始化app信息
        final String SHUNFENG_APP = "shunfeng1234";
        APPInfo appInfo = new APPInfo(SHUNFENG_APP, "shunfeng");
        appInfo.setNotifyUrl(APPConsts.NOTIFY_URL);
        APPConsts.appInfoMap.put(SHUNFENG_APP, appInfo);
    }

}
