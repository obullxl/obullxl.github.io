/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;

import com.atom.crwal.Consts;

/**
 * 系统参数工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: ConfigUtils.java, 2012-5-19 下午11:06:21 Exp $
 */
public class ConfigUtils {

    /**
     * 系统参数
     */
    private static final Map<String, String> cfgs = initProperties();

    /**
     * 初始化系统参数
     */
    private static final Map<String, String> initProperties() {
        Map<String, String> map = new HashMap<String, String>();

        String path = Consts.ROOT + "/config/system-config.xml";
        Properties props = new Properties();
        // 从指定的文件读取配置
        try {
            InputStream is = null;
            File file = new File(path);
            if (file != null && file.exists()) {
                try {
                    is = new FileInputStream(file);
                    props.loadFromXML(is);
                } finally {
                    IOUtils.closeQuietly(is);
                }
            } else {
                try {
                    is = ConfigUtils.class.getResourceAsStream("/system-config.xml");
                    props.loadFromXML(is);
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }

            // 初始化
            initConfigs(map, props);

            // 成功，直接返回
            return map;
        } catch (Exception e) {
            throw new RuntimeException("系统参数初始化失败！", e);
        }
    }

    /**
     * 初始化
     */
    private static final void initConfigs(Map<String, String> map, Properties props) {
        for (Object key : props.keySet()) {
            String skey = ObjectUtils.toString(key);
            map.put(skey, props.getProperty(skey));
        }
    }

    /**
     * 获取参数
     */
    public static final String findConfig(String key) {
        return cfgs.get(key);
    }

    /**
     * 获取Web主站点
     */
    public static final String findHost() {
        return findConfig("HOST");
    }

}
