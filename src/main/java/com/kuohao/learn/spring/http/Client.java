package com.kuohao.learn.spring.http;

import com.kuohao.learn.spring.http.consts.APPConsts;
import com.kuohao.learn.spring.http.consts.HttpHeader;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/7/27
 * Time: 下午7:37
 * CopyRight: taobao
 * Descrption:
 */

public class Client {
    public  static  String APP_KEY="shunfeng1234";     //服务端告诉给客户端的,每个客户端都是固定死的
    public  static  String APP_SECRET="";     //secret和token都是动态变化的,
    public  static  String APP_TOKEN="";


    public static void main(String[] args) {

//        testPostForm();
        testPostJson();
//        testSimplePost();
    }

    //测试json入参
    public static void testPostJson() {
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
            String token="";
            //请求
            HttpResponse response = HttpUtil.httpPost(url, headers, body, Client.APP_KEY, Client.APP_SECRET, Client.APP_TOKEN,timeout);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                //success
                HttpEntity httpEntity = response.getEntity();
                String secretResponse = EntityUtils.toString(httpEntity);//取出应答字符串
                System.out.println("result 结果=" + secretResponse);

                String decryptResponse = AESTool.aesDecrypt(secretResponse, APPConsts.APPSECRET);
                System.out.println("decryptResponse=" + decryptResponse);
            }
            response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试form入参
    private static void testPostForm() {
        String url = "http://localhost:8080/springboot/sayNamePost/zhangsan/roleId/123";

        //定义header
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        headers.put(HttpHeader.HTTP_HEADER_DATE, LocalDate.now().toString());
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, "text/html; charset=utf-8");
        //定义post入参
        Map<String, String> bodyParam = new HashMap<String, String>();
        bodyParam.put("name", "zhangsan");
        bodyParam.put("userId", "0001");
        //appkey和appsecret
        String appKey = "shunfeng1234";
        String appSecret = "123456";
        int timeout = 1000;
        try {
            HttpResponse response = HttpUtil.httpPost(url, headers, bodyParam, "1.0", Client.APP_KEY, Client.APP_SECRET, Client.APP_TOKEN, timeout);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                //success
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);//取出应答字符串
                System.out.println("result 结果=" + result);
            }
            System.out.println("response=" + response);
            response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSimplePost() {
        String url = "http://localhost:8080/notify/token";
        //:发送普通http+post请求,不用加密,
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(APPConsts.APP_SECRET_KEY, "123");
        paramMap.put(APPConsts.APP_TOKE_KEY, "456");
            HttpResponse response = null;
            try {
                response = HttpUtil.simplePost(url, paramMap, 1000);
                System.out.println("response=" + response.getStatusLine());
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    }
