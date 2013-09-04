/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;

/**
 * HTTP工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: HttpUtils.java, 2012-5-20 上午11:58:46 Exp $
 */
public class HttpUtils {

    /**
     * HTTP客户端
     */
    private static final HttpClient httpClient = initHttpClient();

    /** 初始化 */
    private static final HttpClient initHttpClient() {
        HttpConnectionManager connMan = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams par = new HttpConnectionManagerParams();
        connMan.setParams(par);
        return new HttpClient(connMan);
    }

    /**
     * 获取HTTP客户端
     */
    public static final HttpClient findHttpClient() {
        return httpClient;
    }

    /**
     * 获取GET内容
     */
    public static final String get(GetMethod get) throws Exception {
        try {
            findHttpClient().executeMethod(get);
            return IOUtils.toString(get.getResponseBodyAsStream(), "UTF-8");
        } finally {
            get.releaseConnection();
        }
    }

    /**
     * 获取POST内容
     */
    public static final String post(PostMethod post) throws Exception {
        try {
            findHttpClient().executeMethod(post);
            return IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
        } finally {
            post.releaseConnection();
        }
    }

}
