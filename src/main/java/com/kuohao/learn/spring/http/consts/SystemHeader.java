/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.kuohao.learn.spring.http.consts;

/**
 * ϵͳHTTPͷ����
 */
public class SystemHeader {
    //ǩ������Сд��,���������õ�������ͷ����תΪСд,�ܿ�

    //version
    public static final String X_CA_VERSION = "x-ca-version";
    //����ʱ���
    public static final String X_CA_TIMESTAMP = "x-ca-timestamp";
    //APP KEY
    public static final String X_CA_KEY = "x-ca-appkey";
    //token
    public static final String X_CA_TOKEN = "x-ca-token";

    //ǩ��Header
    public static final String X_CA_SIGNATURE = "x-sa-sign";
    //���в���ǩ����Header
    public static final String X_CA_SIGNATURE_HEADERS = "x-cb-signature-headers";
}
