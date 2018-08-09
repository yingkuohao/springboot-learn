package com.kuohao.learn.spring.controller;

import com.alibaba.fastjson.JSONObject;
import com.kuohao.learn.spring.http.*;
import com.kuohao.learn.spring.http.baseobj.RpcServiceResult;
import com.kuohao.learn.spring.http.consts.APPConsts;
import com.kuohao.learn.spring.http.consts.HttpHeader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/8/8
 * Time: 下午3:31
 * CopyRight: taobao
 * Descrption:
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/client")
public class ClientController {
    private final static Logger logger = LoggerFactory.getLogger(ClientController.class);

    //测试json入参         http://localhost:8080/client/testRequest?token=b1e26154-f859-4e75-91b4-e2dbc50d695d
    @RequestMapping(value = "/testRequest", method = RequestMethod.GET)
    @ResponseBody
    public String testPostJson(@RequestParam("token") String token) {
//        String url = "http://localhost:8080/springboot/sayNamePostStr/zhangsan/roleId/123";
        String url = "http://localhost:8080/springboot/sayHello/zhangsan/";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "*/*");
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, "text/html; charset=utf-8");
        headers.put(HttpHeader.HTTP_HEADER_DATE, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        String body = "{\n" +
                "\"name\": \"zhangsan\",\n" +
                "\"userId\": \"0001\"\n" +
                "}";
        int timeout = 1000;
        try {
            //获取token.
            if (StringUtils.isEmpty(token)) {
                token = Client.APP_TOKEN;
            }
            if (StringUtils.isEmpty(Client.APP_SECRET) || StringUtils.isEmpty(Client.APP_TOKEN)) {
                logger.error("appsecret or token empty!please refresh!");
                return "error";
            }

            //请求
            HttpResponse response = HttpUtil.httpPost(url, headers, body, Client.APP_KEY, Client.APP_SECRET, token, timeout);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                //success
                HttpEntity httpEntity = response.getEntity();
                String secretResponse = EntityUtils.toString(httpEntity);//取出应答字符串
                String decryptResponse = AESTool.aesDecrypt(String.valueOf(secretResponse), Client.APP_SECRET);
                System.out.println("result 结果=" + secretResponse);
                RpcServiceResult jsonObject = JSONObject.parseObject(decryptResponse, RpcServiceResult.class);
                if (jsonObject.isSuccess()) {
                    String datas = jsonObject.getDatas().toString();
                    System.out.println("datas=" + datas);
                } else {
                    System.out.println("errorcode=" + jsonObject.getCode() + ",errormsg=" + jsonObject.getMsg());
                }
            }
            response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }


    //    http://localhost:8080/client/notify/token"
    @RequestMapping(value = "/notify/token", method = RequestMethod.POST)
    @ResponseBody
    public String sayUser(HttpServletRequest request) {
        String appSecret = request.getParameter(APPConsts.APP_SECRET_KEY);
        String appToken = request.getParameter(APPConsts.APP_TOKE_KEY);
        //客户端要更新本地内存
        Client.APP_SECRET = appSecret;
        Client.APP_TOKEN = appToken;
        logger.info("appSecret;" + appSecret);
        logger.info("appToken;" + appToken);
        return "ok";
    }
}
