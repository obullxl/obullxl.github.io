/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.atom.crwal.Consts;

/**
 * ID工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: IDUtils.java, 2012-5-20 下午5:25:21 Exp $
 */
public class IDUtils {
    /** 当前最大值KEY */
    public static final String         MAX_KEY = "__MAX__";

    /** 当前值 */
    private static final AtomicLong    ID      = new AtomicLong(1L);

    /** 步长 */
    private static final int           STEP    = 20;

    /** 最大值 */
    private static long                MAX     = ID.get() + STEP;

    /** 读写锁 */
    private static final ReadWriteLock lock    = new ReentrantReadWriteLock();

    /**
     * 配置文件路径
     */
    private static final String findCfgFile() throws Exception {
        String root = Consts.ROOT;

        // 确保目录存在
        String path = FilenameUtils.normalize(root + "/config");
        File pfile = new File(path);
        FileUtils.forceMkdir(pfile);

        return FilenameUtils.normalize(root + "/config/cfg-id.map");
    }

    /**
     * 初始化
     */
    public static final void initCfgID() throws Exception {
        lock.writeLock().lock();
        try {
            // 读取文件
            String fpath = findCfgFile();
            File file = new File(fpath);
            if (!file.exists()) {
                return;
            }

            // 读取内容
            Map<String, String> map = NapUtils.loadMapFile(fpath);

            // 找出最大值
            MAX = MapUtils.getLong(map, MAX_KEY, MAX) + STEP;

            ID.set(MAX);

            MAX += STEP;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 获取下一个ID
     */
    public static final long nextValue() throws Exception {
        lock.writeLock().lock();
        try {
            long value = ID.getAndIncrement();

            if (value >= MAX) {
                // 存储当前值
                Map<String, String> maps = new HashMap<String, String>();
                maps.put(MAX_KEY, Long.toString(value));
                NapUtils.storeMapFile(maps, findCfgFile());

                // 更新当前最大值
                MAX += STEP;
            }

            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 产品ID
     */
//    public static final String findProduceID(String bid, String tid, String nid, String vmodel) {
//        StringBuilder txt = new StringBuilder(20);
//
//        if (StringUtils.isNotBlank(bid)) {
//            txt.append(StringUtils.trim(bid));
//        }
//
//        if (StringUtils.isNotBlank(tid)) {
//            if (txt.length() > 0) {
//                txt.append("_");
//            }
//            txt.append(StringUtils.trim(tid));
//        }
//
//        if (StringUtils.isNotBlank(nid)) {
//            if (txt.length() > 0) {
//                txt.append("_");
//            }
//            txt.append(StringUtils.trim(nid));
//        }
//
//        if (StringUtils.isNotBlank(vmodel)) {
//            if (txt.length() > 0) {
//                txt.append("_");
//            }
//            txt.append(StringUtils.trim(vmodel));
//        }
//
//        return txt.toString();
//    }

}
