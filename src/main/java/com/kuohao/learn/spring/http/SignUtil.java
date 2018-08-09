package com.kuohao.learn.spring.http;

import com.kuohao.learn.spring.http.consts.HttpHeader;
import com.kuohao.learn.spring.http.consts.SystemHeader;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/7/27
 * Time: 下午7:47
 * CopyRight: taobao
 * Descrption:
 */

public class SignUtil {
    /**
     * 计算签名
     *
     * @param method       HttpMethod
     * @param url          Path+Query
     * @param headers      Http头
     * @param formParamMap POST表单参数
     * @param secret       APP密钥
     * @return 签名后的字符串
     */
    public static String signForm(String method, String url, Map<String, String> headers, Map formParamMap, String secret) {
        try {

            String strToSign = buildStringToSign(headers, url, formParamMap, method);
            System.out.println("--- strToSign=" + strToSign);
            String md5Sign = MD5Util.getMD5String(strToSign);
            System.out.println("--- md5Sign=" + md5Sign);
            return md5Sign;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String signJson(String method, String url, Map<String, String> headers, String body, String secret) {
        try {

            String strToSign = buildStringToSign(headers, url, body, method);
            System.out.println("--- strToSign=" + strToSign);
            String md5Sign = MD5Util.getMD5String(strToSign);
            System.out.println("--- md5Sign=" + md5Sign);
            return md5Sign;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //换行符
    public static final String LF = "\n";

    /**
     * 构建待签名字符串
     *
     * @param headers      Http头
     * @param url          Path+Query
     * @param formParamMap POST表单参数
     * @param method
     * @return 签名字符串
     */
    private static String buildStringToSign(Map<String, String> headers, String url, Map formParamMap, String method) {
        StringBuilder sb = new StringBuilder();
        //标准协议头
        buildBasicHeader(headers, method, sb);
        sb.append(buildSystemHeader(headers));         //header中固定参数
        sb.append(buildResource(url, formParamMap));            //参数封装

        return sb.toString();
    }

    private static String buildStringToSign(Map<String, String> headers, String url, String body, String method) {
        StringBuilder sb = new StringBuilder(url);
        sb.append(LF);
        System.out.println("---url info--:" + sb
                .toString());
//        buildBasicHeader(headers, method, sb);      //标准协议头  todo:有大小写问题,先不加
        sb.append(buildSystemHeader(headers));         //header中固定参数
        System.out.println("---systemHeader info--:" + sb.toString());
        sb.append(body);            //参数封装
        System.out.println("---body info--:" + sb.toString());

        return sb.toString();
    }

    private static void buildBasicHeader(Map<String, String> headers, String method, StringBuilder sb) {
        sb.append(method.toUpperCase()).append(LF);
        if (headers.get(HttpHeader.HTTP_HEADER_ACCEPT) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_ACCEPT));
            sb.append(LF);
        }
      /*  if (headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5));
            sb.append(LF);
        }*/
        if (headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE));
            sb.append(LF);
        }
        if (headers.get(HttpHeader.HTTP_HEADER_DATE) != null) {
            sb.append(headers.get(HttpHeader.HTTP_HEADER_DATE));
            sb.append(LF);
        }
    }


    /* 构建待签名Http头
       *
       * @param headers              请求中所有的Http头
       * @return 待签名Http头
       */
    private static String buildSystemHeader(Map<String, String> headers) {
        Map<String, String> headersToSign = new TreeMap<String, String>();

        if (headers != null) {
            StringBuilder signHeadersStringBuilder = new StringBuilder();

            int flag = 0;
            //参数Key按字典排序
            Map<String, String> sortMap = new TreeMap<String, String>();
            sortMap.putAll(headers);
            for (Map.Entry<String, String> header : sortMap.entrySet()) {
                //如果header的key满足签名
                if (isHeaderToSign(header.getKey())) {
                    if (flag != 0) {
                        signHeadersStringBuilder.append(",");
                    }
                    flag++;
                    signHeadersStringBuilder.append(header.getKey());
                    headersToSign.put(header.getKey(), header.getValue());
                }
            }
            if (!headers.containsKey(SystemHeader.X_CA_SIGNATURE_HEADERS)) {

                headers.put(SystemHeader.X_CA_SIGNATURE_HEADERS, signHeadersStringBuilder.toString());
            }
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> e : headersToSign.entrySet()) {
            sb.append(e.getKey()).append(':').append(e.getValue()).append(LF);
        }

        return sb.toString();
    }

    //参与签名的系统Header前缀,只有指定前缀的Header才会参与到签名中
    public static final String CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "x-ca-";

    /**
     * Http头是否参与签名
     * return
     */
    private static boolean isHeaderToSign(String headerName) {

        if (StringUtils.isBlank(headerName)) {
            return false;
        }

        if (headerName.startsWith(CA_HEADER_TO_SIGN_PREFIX_SYSTEM)) {
            return true;
        }


        return false;
    }

    /**
     * 构建待签名Path+Query+FormParams
     *
     * @param url          Path+Query
     * @param formParamMap POST表单参数
     * @return 待签名Path+Query+FormParams
     */
    private static String buildResource(String url, Map formParamMap) {
        if (url.contains("?")) {
            String path = url.split("\\?")[0];
            String queryString = url.split("\\?")[1];
            url = path;
            if (formParamMap == null) {
                formParamMap = new HashMap();
            }
            if (StringUtils.isNotBlank(queryString)) {
                for (String query : queryString.split("\\&")) {
                    String key = query.split("\\=")[0];
                    String value = "";
                    if (query.split("\\=").length == 2) {
                        value = query.split("\\=")[1];
                    }
                    if (formParamMap.get(key) == null) {
                        formParamMap.put(key, value);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(url);

        if (formParamMap != null && formParamMap.size() > 0) {
            sb.append('?');

            //参数Key按字典排序
            Map<String, String> sortMap = new TreeMap<String, String>();
            sortMap.putAll(formParamMap);

            int flag = 0;
            for (Map.Entry<String, String> e : sortMap.entrySet()) {
                if (flag != 0) {
                    sb.append('&');
                }

                flag++;
                String key = e.getKey();
                String val = e.getValue();

                if (val == null || ((val instanceof String) && StringUtils.isBlank(val))) {
                    sb.append(key);
                } else {
                    sb.append(key).append("=").append(val);
                }
            }
        }

        return sb.toString();
    }


}
