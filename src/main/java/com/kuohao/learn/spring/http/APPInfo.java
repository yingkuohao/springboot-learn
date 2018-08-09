package com.kuohao.learn.spring.http;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/7/29
 * Time: 上午10:36
 * CopyRight: taobao
 * Descrption:
 */

public class APPInfo {
    private String appKey;
    private String appSecret;                                    //当前密钥
    private String lastAppSecret;        //上一个密钥
    private String token;                                     //最新token
    private String lastToken;     //上一个token
    private long timstamp;
    private String name;
    private String notifyUrl;

    public APPInfo() {
    }

    public APPInfo(String appKey, String name) {
        this.appKey = appKey;
        this.name = name;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(long timstamp) {
        this.timstamp = timstamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public String getLastAppSecret() {
        return lastAppSecret;
    }

    public void setLastAppSecret(String lastAppSecret) {
        this.lastAppSecret = lastAppSecret;
    }

    @Override
    public String toString() {//yingkhtodo:desc:      通用基类
        return "APPInfo{" +
                "appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", token='" + token + '\'' +
                ", lastToken='" + lastToken + '\'' +
                ", timstamp=" + timstamp +
                ", name='" + name + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
