package com.kuohao.learn.spring.http.baseobj;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/8/8
 * Time: 上午11:12
 * CopyRight: taobao
 * Descrption:
 */

public class BaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

   	public String toString(){
   		return ToStringBuilder.reflectionToString(this);
   	}
}
