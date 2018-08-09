package com.kuohao.learn.spring.controller;

import com.kuohao.learn.spring.config.User;
import com.kuohao.learn.spring.http.APPInfo;
import com.kuohao.learn.spring.http.AccessUtil;
import com.kuohao.learn.spring.http.Client;
import com.kuohao.learn.spring.http.HttpUtil;
import com.kuohao.learn.spring.http.consts.APPConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午2:41
 * CopyRight: taobao
 * Descrption:      RestController 注解,其实就是ResponseBody和controller注解的整合
 */

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/server")
public class TokenController {


    //定义一个全局的记录器，通过LoggerFactory获取
    private final static Logger logger = LoggerFactory.getLogger(TokenController.class);
    @Autowired
    User user;



    //    http://localhost:8080/server/getToken?appKey=shunfeng1234
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ResponseBody
    public String getToken(@RequestParam("appKey") String appKey) {
        System.out.println("appKey: " + appKey);
        String result = "";
        //yingkhtodo: 通用的返回值和返回code封装,用http的code来表示系统错误
        boolean bool = AccessUtil.refreshToken(appKey);
        if (!bool) {
            result = "getToken failure,reason= refreshTokenError";
        } else {
            APPInfo appInfo = APPConsts.appInfoMap.get(appKey);
            logger.info("---appinfo=" + appInfo.toString());
            String url = appInfo.getNotifyUrl();
            //:发送普通http+post请求,不用加密,
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(APPConsts.APP_SECRET_KEY, appInfo.getAppSecret());
            paramMap.put(APPConsts.APP_TOKE_KEY, appInfo.getToken());
            try {
                HttpResponse response = HttpUtil.simplePost(url, paramMap, 1000);
                System.out.println("response=" + response.getStatusLine());
                if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                    //success
                    HttpEntity httpEntity = response.getEntity();
                    String secretResponse = EntityUtils.toString(httpEntity);
                    System.out.println("result 结果=" + secretResponse);
                    result = "getToken success";
                } else {
                    logger.error("notify token failer");
                    result = "getToken failure, reason= notfiy failure";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        logger.info("result:" + result);
        return result;
    }

}
