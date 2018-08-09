package com.kuohao.learn.spring.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.kuohao.learn.spring.http.AESTool;
import com.kuohao.learn.spring.http.APPInfo;
import com.kuohao.learn.spring.http.baseobj.BaseResultCode;
import com.kuohao.learn.spring.http.baseobj.RpcServiceResult;
import com.kuohao.learn.spring.http.consts.SystemHeader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/11/2
 * Time: 下午7:55
 * CopyRight: taobao
 * Descrption:
 */
@Aspect   //定义一个切面
@Configuration
public class AuthAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);

    // 定义切点Pointcut
    @Pointcut("execution(* com.alicp.jkc.gateway.controller.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String params = "";
        //可以获取所有参数,但是不能确定是哪个
     /*   if ("POST".equals(method)) {
            Object[] paramsArray = pjp.getArgs();
            pjp.getSignature();
            params = argsArrayToString(paramsArray);
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            params = paramsMap.toString();
        }*/
        logger.info("请求开始, 各个参数, url: {}, method: {}, uri: {}, params: {}", url, method, uri, params);
        String appKey = request.getHeader(SystemHeader.X_CA_KEY);
        RpcServiceResult<String> result = hasPermission(request);
        if (result.isSuccess()) {
            try {
                String content = "";
                Object realResult = pjp.proceed();
                if (realResult != null) {
                    content = JSONObject.toJSONString(realResult);
                } else {
                    result.setSuccess(false);
                    result.setCode(BaseResultCode.DATA_EMPTY.getCode());
                    result.setMsg(BaseResultCode.DATA_EMPTY.getMsg());
                    content = JSONObject.toJSONString(result);
                }
                logger.info("aesdecrypt resultStr={}", content);
                String appSecret = (String) request.getAttribute(IntercpConsts.APP_SECRET);
                String resultStr = AESTool.aesEncrypt(content, appSecret);
                logger.info("aesEncrypt resultStr={}", resultStr);
                return resultStr;
            } catch (Exception e) {
                logger.error(" aop error, uri={},method={}", uri, method);
                result.setSuccess(false);
                result.setCode(BaseResultCode.INTERNAL_ERROR.getCode());
                result.setMsg(BaseResultCode.INTERNAL_ERROR.getMsg());
            }
        }
        // result的值就是被拦截方法的返回值
        logger.info("请求结束，controller的返回值是 " + result);
        return result;
    }


    /**
     * 请求参数拼装
     *
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                Object jsonObj = JSON.toJSON(paramsArray[i]);
                params += jsonObj.toString() + " ";
            }
        }
        return params.trim();
    }

    private RpcServiceResult<String> hasPermission(HttpServletRequest request) throws IOException {
        logger.info("preHandle execute," + request.getRequestURI());
        String appKey = request.getHeader(SystemHeader.X_CA_KEY);
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        String body = HttpHelper.getBodyString(requestWrapper);
        RpcServiceResult<String> result = new RpcServiceResult<String>();
        APPInfo appInfo = null;
        if ((appInfo = getAppinfo(result, appKey)) == null) {
            result.setSuccess(false);
            result.setCode(BaseResultCode.APP_ERROR.getCode());
            result.setMsg(BaseResultCode.APP_ERROR.getMsg());
            return result;
        }

        try {
            result = checkParamsLogic(body);
            if (!result.isSuccess()) {
                logger.error("json parse error, body = {}", body);
                result.setSuccess(false);
                result.setCode(BaseResultCode.ILLEGAL_PARAM.getCode());
                result.setMsg(BaseResultCode.ILLEGAL_PARAM.getMsg());
                result.setDatas("请求参数解析json失败");
                return result;
            }

            //得到请求密文
            String decrpytBody = AESTool.aesDecrypt(body, appInfo.getAppSecret());
            logger.info("request params encrpyt = {}, decrpytBody = {}", body, decrpytBody);
            request.setAttribute(IntercpConsts.APP_SECRET, appInfo.getAppSecret());
            result.setSuccess(true);
            result.setCode(BaseResultCode.OK.getCode());
            result.setMsg(BaseResultCode.OK.getMsg());
            return result;
        } catch (Exception e) {
            logger.error("prehanderl 解密失败,appInfo={} ", appInfo.toString());
            result.setSuccess(false);
            result.setCode(BaseResultCode.APP_ERROR.getCode());
            result.setMsg(BaseResultCode.APP_ERROR.getMsg());
        }

        return result;
    }


    private RpcServiceResult<String> checkParamsLogic(String body) {
        //:desc:参数校验
        RpcServiceResult<String> result = new RpcServiceResult<String>();
        result.setSuccess(true);
        try {
            Preconditions.checkNotNull(body, "body is null");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            result.setCode(BaseResultCode.ILLEGAL_PARAM.getCode());
        }
        return result;
    }

    private APPInfo getAppinfo(RpcServiceResult<String> result, String appKey) {
//        APPInfo appInfo = accessUtil.getAppInfoByCache(appKey);
        APPInfo appInfo = new APPInfo();
        if (appInfo == null) {
            result.setCode(BaseResultCode.ACCESS_ERROR.getCode());
            result.setMsg(BaseResultCode.ACCESS_ERROR.getMsg());
            result.setDatas("token invalid!");
            logger.warn("appInfo is null, appKey = {}", appKey);
        }
        return appInfo;
    }

}
