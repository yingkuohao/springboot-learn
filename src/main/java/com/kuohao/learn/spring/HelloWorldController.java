package com.kuohao.learn.spring;

import com.alibaba.fastjson.JSONObject;
import com.kuohao.learn.spring.config.Address;
import com.kuohao.learn.spring.config.User;
import com.kuohao.learn.spring.http.*;
import com.kuohao.learn.spring.http.baseobj.BaseResultCode;
import com.kuohao.learn.spring.http.baseobj.RpcServiceResult;
import com.kuohao.learn.spring.http.consts.APPConsts;
import com.kuohao.learn.spring.http.consts.SystemHeader;
import com.kuohao.learn.spring.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/4/28
 * Time: 下午2:20
 * CopyRight: taobao
 * Descrption:
 * hello world 例子
 * http://localhost:8080/springboot/liaokailin
 */

@RestController
@RequestMapping("/springboot")
public class HelloWorldController {

    //定义一个全局的记录器，通过LoggerFactory获取
    private final static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);
    @Autowired
    User user;
    @Autowired
    Address address;

    @Autowired
    TestService testService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseBody
    public String sayWorld(@PathVariable("name") String name) {
        System.out.println("userName;" + user.getName());
        System.out.println("user;" + user.toString());
        System.out.println("user;" + address.toString());
        logger.info("test log " + user.getName());
        testService.sayHello();
        return "hello " + name;
    }


    @RequestMapping(value = "/sayUser", method = RequestMethod.GET)
    @ResponseBody
    public User sayUser(String name) {
        System.out.println("userName;" + user.getName());
        System.out.println("user;" + user.toString());
        System.out.println("user;" + address.toString());
        logger.info("test log " + user.getName());
        return user;
    }

    //    http://localhost:8080/springboot/name/zhangsan/roleId/123
    @RequestMapping(value = "/name/{name}/roleId/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String sayName(@PathVariable("name") String name, @PathVariable("userId") String userId) {
        System.out.println("userName;" + user.getName());
        System.out.println("user;" + user.toString());
        System.out.println("user;" + address.toString());
        logger.info("test log " + user.getName());
        testService.sayHello();
        return "hello " + name;
    }

    //    http://localhost:8080/springboot/sayNamePost/zhangsan/roleId/123
    @RequestMapping(value = "/sayNamePostForm/{name}/roleId/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public String sayNamePostForm(HttpServletRequest request) {

        String body = request.getParameter("body");
        try {
            String secretBody = AESTool.aesDecrypt(body, APPConsts.APPSECRET);
            System.out.println("secretBody;" + secretBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String name = request.getParameter("name");
        String userId = request.getParameter("userId");
        System.out.println("userName;" + name);
        System.out.println("userId;" + userId);
        logger.info("test log " + user.getName());
        testService.sayHello();

        return "hello " + name;
    }

    //    http://localhost:8080/springboot/sayHello/zhangsan/
    @RequestMapping(value = "/sayHello/{name}", method = RequestMethod.POST)
    @ResponseBody
    public String sayNamePostStr(@RequestBody String body, HttpServletRequest request, @RequestHeader("x-ca-token") String appToken, @RequestHeader("x-ca-appkey") String appKey) {

        RpcServiceResult<String> result = new RpcServiceResult<String>();
        result.setSuccess(false);
        String name = "";
        String response = "";
        String resultStr = "";
        String appsecret = AccessUtil.checkAppToken(appKey, appToken);       //这里注意,token和secret要成对,如果客户端传过来的是旧token,这里也要取旧的密钥进行加密
        if (appsecret == null) {
            result.setCode(BaseResultCode.ACCESS_ERROR.getCode());
            result.setMsg(BaseResultCode.ACCESS_ERROR.getMsg());
            result.setDatas("token invalid!");
            return JSONObject.toJSONString(result);
        }
        try {

            //用客户端同样的算法计算签名
            Enumeration headers = request.getHeaderNames();
            Map<String, String> headerMap = new HashMap<String, String>();
            while ((headers.hasMoreElements())) {
                String key = (String) headers.nextElement();
                headerMap.put(key, request.getHeader(key));
            }

            String systemSign = SignUtil.signJson("POST", request.getRequestURI().toString(), headerMap, body, appsecret);
            String clientSign = request.getHeader(SystemHeader.X_CA_SIGNATURE);
            System.out.println("---clientSign=" + clientSign);
            System.out.println("---systemSign=" + systemSign);
            //如果签名与客户端header中的相等,说明没被篡改,则
            if (systemSign != null && systemSign.equals(clientSign)) {
                System.out.println("sign ok!");
                //得到请求密文
                String secretBody = AESTool.aesDecrypt(body, appsecret);
                JSONObject jsonObject = JSONObject.parseObject(secretBody);
                name = jsonObject.getString("name");
                logger.info("---secretBody;" + secretBody);
                //生成响应
                testService.sayHello();
                response = "hello " + name;
                result.setParams(body);
            } else {
                response += "error,sign不合法";
                result.setCode(BaseResultCode.SIGNATURE_ERROR.getCode());
                result.setMsg(BaseResultCode.SIGNATURE_ERROR.getMsg());
                result.setDatas(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResultCode.INTERNAL_ERROR.getCode());
            result.setMsg(BaseResultCode.INTERNAL_ERROR.getMsg());
            result.setDatas("内部错误");
        }
        try {
            logger.info("---response :" + response);
//            encodeResponse = AESTool.aesEncrypt(response, appsecret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setSuccess(true);
        result.setCode(BaseResultCode.OK.getCode());
        result.setMsg(BaseResultCode.OK.getMsg());
        result.setDatas(response);
        try {
            //结果整体加密
            resultStr = AESTool.aesEncrypt(JSONObject.toJSONString(result), appsecret);
            logger.info("---encrypt resultStr :" + resultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


}
