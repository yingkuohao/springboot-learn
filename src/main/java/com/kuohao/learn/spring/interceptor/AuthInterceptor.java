package com.kuohao.learn.spring.interceptor;

import com.google.common.base.Preconditions;
import com.kuohao.learn.spring.http.AESTool;
import com.kuohao.learn.spring.http.APPInfo;
import com.kuohao.learn.spring.http.baseobj.BaseResultCode;
import com.kuohao.learn.spring.http.baseobj.RpcServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/11/2
 * Time: 下午2:37
 * CopyRight: taobao
 * Descrption:     权限拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);



    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle execute,"+httpServletRequest.getRequestURI());
        String appKey = httpServletRequest.getHeader("x-ca-appkey");
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        String body = HttpHelper.getBodyString(requestWrapper);
        RpcServiceResult<String> result = new RpcServiceResult<String>();
        APPInfo appInfo = null;
        if ((appInfo = getAppinfo(result, appKey)) == null) {
            result.setSuccess(false);
            result.setCode(BaseResultCode.APP_ERROR.getCode());
            result.setMsg(BaseResultCode.APP_ERROR.getMsg());
            printResponse(httpServletResponse, result);
            return false;
        }

        try {
            result = checkParamsLogic(body);
            if (!result.isSuccess()) {
                logger.error("json parse error, body = {}", body);
                result.setSuccess(false);
                result.setCode(BaseResultCode.ILLEGAL_PARAM.getCode());
                result.setMsg(BaseResultCode.ILLEGAL_PARAM.getMsg());
                result.setDatas("请求参数解析json失败");
                printResponse(httpServletResponse, result);
                return false;
            }

            //得到请求密文
            String decrpytBody = AESTool.aesDecrypt(body, appInfo.getAppSecret());
            logger.info("request params encrpyt = {}, decrpytBody = {}", body, decrpytBody);
            httpServletRequest.setAttribute(IntercpConsts.DECRPYT_BODY, decrpytBody);
            httpServletRequest.setAttribute(IntercpConsts.APP_SECRET, appInfo.getAppSecret());
            return true;
        } catch (Exception e) {
            logger.error("prehanderl 解密失败,appInfo={} ", appInfo.toString());
            result.setSuccess(false);
            result.setCode(BaseResultCode.APP_ERROR.getCode());
            result.setMsg(BaseResultCode.APP_ERROR.getMsg());
        }

        printResponse(httpServletResponse, result);
        return false;
    }

    private void printResponse(HttpServletResponse httpServletResponse, RpcServiceResult<String> result) {
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.print(result.toString());
        } catch (Exception e) {
            logger.error("PrintWriter open error");
        } finally {
           /* if (out != null) {
                out.close();
            }*/
        }
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

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
        APPInfo appInfo =new APPInfo();
        if (appInfo == null) {
            result.setCode(BaseResultCode.ACCESS_ERROR.getCode());
            result.setMsg(BaseResultCode.ACCESS_ERROR.getMsg());
            result.setDatas("token invalid!");
            logger.warn("appInfo is null, appKey = {}", appKey);
        }
        return appInfo;
    }


}
