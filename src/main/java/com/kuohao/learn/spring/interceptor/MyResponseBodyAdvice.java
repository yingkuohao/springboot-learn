package com.kuohao.learn.spring.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kuohao.learn.spring.http.AESTool;
import com.kuohao.learn.spring.http.baseobj.BaseResultCode;
import com.kuohao.learn.spring.http.baseobj.RpcServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/11/2
 * Time: 下午3:46
 * CopyRight: taobao
 * Descrption:
 */
//@Order(1)
//@ControllerAdvice(basePackages = "com.alicp.jkc.gateway.controller")
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    private static Logger logger = LoggerFactory.getLogger(MyResponseBodyAdvice.class);

    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //这里可以根据自己的需求
        return true;
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String appSecret = (String) servletRequest.getAttribute(IntercpConsts.APP_SECRET);
        RpcServiceResult<String> result = new RpcServiceResult<String>();
        try {
            String resultStr = AESTool.aesEncrypt(JSONObject.toJSONString(o), appSecret);
            logger.info("aesEncrypt resultStr={}", resultStr);
            return resultStr;
        } catch (Exception e) {
            logger.error(" ---aesEncrypt body error,o={},appsecret={},error={}", o, appSecret, e);
            result.setCode(BaseResultCode.INTERNAL_ERROR.getCode());
            result.setMsg(BaseResultCode.INTERNAL_ERROR.getMsg());
            result.setDatas("内部错误");
        }
        return result.toString();
    }


}
