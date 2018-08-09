package com.kuohao.learn.spring.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.kuohao.learn.spring.http.consts.APPConsts;
import com.kuohao.learn.spring.http.consts.ContentType;
import com.kuohao.learn.spring.http.consts.HttpHeader;
import com.kuohao.learn.spring.http.consts.SystemHeader;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/7/27
 * Time: 下午7:37
 * CopyRight: taobao
 * Descrption:
 */

public class HttpUtil {
    //编码UTF-8
    public static final String ENCODING = "UTF-8";
    public static final String VERSION = "1.0";
    public static final String POST = "POST";


    /**
     * Http POST 字符串
     *
     * @param url       http://host+path+query
     * @param headers   Http头
     * @param body      字符串请求体
     * @param appKey    APP KEY
     * @param appSecret APP密钥
     * @param timeout   超时时间（毫秒）
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, String body, String appKey, String appSecret, String token , int timeout) throws Exception {
        HttpPost post = new HttpPost(url);
        String secretBody = null;
        //1.对body题进行AES加密,生成密文
        if (StringUtils.isNotBlank(body)) {
            secretBody = AESTool.aesEncrypt(body, appSecret);
            System.out.println("---body="+body+ "\n----encrypt body=" + secretBody);
            post.setEntity(new StringEntity(secretBody, ENCODING));
        }
        //2.对header及body做签名
        headers = initialBasicHeaderStr(headers, appKey, token,VERSION, appSecret, POST, url, secretBody);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));


        for (Map.Entry<String, String> e : headers.entrySet()) {
            System.out.println("---header info---: "+e.getKey()+":"+e.getValue());
            post.addHeader(e.getKey(), e.getValue());
        }


        return httpClient.execute(post);
    }


    /**
     * HTTP POST表单
     *
     * @param url       http://host+path+query
     * @param headers   Http头
     * @param formParam 表单参数
     * @param appKey    APP KEY
     * @param appSecret APP密钥
     * @param timeout   超时时间（毫秒）
     * @return 调用结果
     * @throws Exception
     */
    public static HttpResponse httpPost(String url, Map<String, String> headers, Map<String, String> formParam, String appKey, String version, String appSecret,String token, int timeout)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_FORM);

        headers = initialBasicHeader(headers, appKey, token,version,appSecret, POST,  url, formParam);

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));

        HttpPost post = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), e.getValue());
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(formParam);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return httpClient.execute(post);
    }

    public static HttpResponse simplePost(String url, Map<String, String> formParam, int timeout) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(timeout));
        HttpPost post = new HttpPost(url);

        UrlEncodedFormEntity formEntity = buildFormEntity(formParam);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }
        HttpResponse response= httpClient.execute(post);
        return response;
    }


    /**
     * 构建FormEntity
     *
     * @param formParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, String> formParam) throws UnsupportedEncodingException {
        if (formParam != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : formParam.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, formParam.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList);
            formEntity.setContentType(ContentType.CONTENT_TYPE_FORM);
            return formEntity;
        }

        return null;
    }


    /**
     * 初始化基础Header
     *
     * @param headers        Http头
     * @param appKey         APP KEY
     * @param appSecret      APP密钥
     * @param method         Http方法
     * @param requestAddress http://host+path+query
     * @param formParam      表单参数
     * @return 基础Header
     * @throws MalformedURLException
     */
    private static Map<String, String> initialBasicHeader(Map<String, String> headers, String appKey, String token ,String version, String appSecret, String method, String requestAddress, Map formParam) throws MalformedURLException {
        StringBuilder stringBuilder = new StringBuilder();
        headers = initCommonHeader(headers, appKey, token,version, requestAddress, stringBuilder);
        headers.put(SystemHeader.X_CA_SIGNATURE, SignUtil.signForm(method, stringBuilder.toString(), headers, formParam, appSecret));

        return headers;
    }


    private static Map<String, String> initialBasicHeaderStr(Map<String, String> headers, String appKey, String token ,String version, String appSecret, String method, String requestAddress, String body) throws MalformedURLException {
        StringBuilder stringBuilder = new StringBuilder();
        //初始化header
        headers = initCommonHeader(headers, appKey, token,version, requestAddress, stringBuilder);
        //把签名放入header
        headers.put(SystemHeader.X_CA_SIGNATURE, SignUtil.signJson(method, stringBuilder.toString(), headers, body, appSecret));

        return headers;
    }

    private static Map<String, String> initCommonHeader(Map<String, String> headers, String appKey, String token ,String version, String requestAddress, StringBuilder stringBuilder) throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        URL url = new URL(requestAddress);
        if (StringUtils.isNotBlank(url.getPath())) {
            stringBuilder.append(url.getPath());
        }
        if (StringUtils.isNotBlank(url.getQuery())) {
            stringBuilder.append("?");
            stringBuilder.append(url.getQuery());
        }
        System.out.println("---url info--:"+stringBuilder.toString());
//         headers.put(HttpHeader.HTTP_HEADER_USER_AGENT, Constants.USER_AGENT);
        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        headers.put(SystemHeader.X_CA_VERSION, version);
        headers.put(SystemHeader.X_CA_KEY, appKey);
        headers.put(SystemHeader.X_CA_TOKEN, token);
        return headers;
    }

    //默认请求超时时间,单位毫秒
    public static final int DEFAULT_TIMEOUT = 1000;

    /**
     * 读取超时时间
     *
     * @param timeout
     * @return
     */
    private static int getTimeout(int timeout) {
        if (timeout == 0) {
            return DEFAULT_TIMEOUT;
        }

        return timeout;
    }
}
