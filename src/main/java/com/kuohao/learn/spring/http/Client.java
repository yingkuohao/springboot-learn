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
 * Time: ����7:37
 * CopyRight: taobao
 * Descrption:
 */

public class Client {
    public  static  String APP_KEY="shunfeng1234";     //����˸��߸��ͻ��˵�,ÿ���ͻ��˶��ǹ̶�����
    public  static  String APP_SECRET="";     //secret��token���Ƕ�̬�仯��,
    public  static  String APP_TOKEN="";


    public static void main(String[] args) {

//        testPostForm();
        testPostJson();
//        testSimplePost();
    }

    //����json���
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
            //��ȡtoken.
            String token="";
            //����
            HttpResponse response = HttpUtil.httpPost(url, headers, body, Client.APP_KEY, Client.APP_SECRET, Client.APP_TOKEN,timeout);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                //success
                HttpEntity httpEntity = response.getEntity();
                String secretResponse = EntityUtils.toString(httpEntity);//ȡ��Ӧ���ַ���
                System.out.println("result ���=" + secretResponse);

                String decryptResponse = AESTool.aesDecrypt(secretResponse, APPConsts.APPSECRET);
                System.out.println("decryptResponse=" + decryptResponse);
            }
            response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //����form���
    private static void testPostForm() {
        String url = "http://localhost:8080/springboot/sayNamePost/zhangsan/roleId/123";

        //����header
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
        headers.put(HttpHeader.HTTP_HEADER_DATE, LocalDate.now().toString());
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, "text/html; charset=utf-8");
        //����post���
        Map<String, String> bodyParam = new HashMap<String, String>();
        bodyParam.put("name", "zhangsan");
        bodyParam.put("userId", "0001");
        //appkey��appsecret
        String appKey = "shunfeng1234";
        String appSecret = "123456";
        int timeout = 1000;
        try {
            HttpResponse response = HttpUtil.httpPost(url, headers, bodyParam, "1.0", Client.APP_KEY, Client.APP_SECRET, Client.APP_TOKEN, timeout);
            if (response != null && 200 == response.getStatusLine().getStatusCode()) {
                //success
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);//ȡ��Ӧ���ַ���
                System.out.println("result ���=" + result);
            }
            System.out.println("response=" + response);
            response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSimplePost() {
        String url = "http://localhost:8080/notify/token";
        //:������ͨhttp+post����,���ü���,
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
