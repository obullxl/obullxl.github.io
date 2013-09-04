/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 映射文件工具类，为了避免与MapUtils重名，故为NapUtils
 * 
 * @author obullxl@gmail.com
 * @version $Id: NapUtils.java, 2012-5-25 下午8:07:18 Exp $
 */
public class NapUtils {

    /**
     * 加载Map文件
     */
    public static final Map<String, String> loadMapFile(String filepath) throws Exception {
        return loadMapFile(filepath, ":");
    }

    /**
     * 加载Map文件
     */
    public static final Map<String, String> loadMapFile(String filepath, String separator) throws Exception {
        // 映射值
        Map<String, String> maps = new ConcurrentHashMap<String, String>();

        // 读取文件
        String fpath = FilenameUtils.normalize(filepath);
        File file = new File(fpath);
        if (!file.exists()) {
            return maps;
        }

        InputStream is = new FileInputStream(fpath);
        List<String> lines = IOUtils.readLines(is);
        IOUtils.closeQuietly(is);

        for (String line : lines) {
            if (StringUtils.isBlank(line) || StringUtils.startsWith(line, "#")) {
                continue;
            }

            String[] tmps = StringUtils.split(line, ":", 2);
            if (tmps == null || tmps.length != 2) {
                continue;
            }

            // Key/Value
            maps.put(StringUtils.trim(tmps[0]), StringUtils.trim(tmps[1]));
        }

        // 返回
        return maps;
    }

    /**
     * 存储配置文件
     */
    public static final void storeIntMapFile(Map<String, Integer> maps, String filepath) throws Exception {
        Map<String, String> tmpmap = new ConcurrentHashMap<String, String>();
        for (Map.Entry<String, Integer> kv : maps.entrySet()) {
            tmpmap.put(kv.getKey(), Integer.toString(kv.getValue()));
        }
        
        storeMapFile(tmpmap, filepath, ":");
    }

    /**
     * 存储配置文件
     */
    public static final void storeMapFile(Map<String, String> maps, String filepath) throws Exception {
        storeMapFile(maps, filepath, ":");
    }

    /**
     * 存储配置文件
     */
    public static final void storeMapFile(Map<String, String> maps, String filepath, String separator) throws Exception {
        OutputStream os = new FileOutputStream(filepath);
        List<String> lines = new ArrayList<String>();

        for (Map.Entry<String, String> entry : maps.entrySet()) {
            lines.add(entry.getKey() + separator + entry.getValue());
        }

        IOUtils.writeLines(lines, null, os, "UTF-8");
        IOUtils.closeQuietly(os);
    }

}
