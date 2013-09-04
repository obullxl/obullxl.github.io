/**
 * aptech.com Inc.
 * Copyright (c) 2008-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.atom.crwal.Consts;

/**
 * 尺寸工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: SizeUtils.java, 2012-5-23 下午12:18:19 Exp $
 */
public class SizeUtils {
    /** 当前最大值KEY */
    public static final String               MAX_KEY = "__MAX__";

    /** 读写锁 */
    private static final ReadWriteLock       lock    = new ReentrantReadWriteLock();

    /** 映射关系 */
    public static final Map<String, Integer> SIZES   = new ConcurrentHashMap<String, Integer>();

    /**
     * 配置文件路径
     */
    private static final String findCfgFile() throws Exception {
        String root = Consts.ROOT;

        // 确保目录存在
        String path = FilenameUtils.normalize(root + "/config");
        File pfile = new File(path);
        FileUtils.forceMkdir(pfile);

        return FilenameUtils.normalize(root + "/config/size-map.map");
    }

    /**
     * 初始化
     */
    public static final void initSizeMap() throws Exception {
        // 读取文件
        String fpath = findCfgFile();
        File file = new File(fpath);
        if (!file.exists()) {
            return;
        }

        // 读取内容
        Map<String, String> maps = NapUtils.loadMapFile(fpath);
        for (Map.Entry<String, String> kv : maps.entrySet()) {
            String value = kv.getValue();
            if (StringUtils.isNumeric(value)) {
                SIZES.put(kv.getKey(), Integer.parseInt(value));
            }
        }

        // 找出最大值
        findMaxValue();
    }

    /**
     * 找出最大值
     */
    public static final int findMaxValue() throws Exception {
        lock.writeLock().lock();
        try {
            if (SIZES.containsKey(MAX_KEY)) {
                return SIZES.get(MAX_KEY);
            }

            // 计算出最大值
            int max = 1;
            if (!SIZES.isEmpty()) {
                for (int value : SIZES.values()) {
                    if (value > max) {
                        max = value;
                    }
                }
            }

            // 更新
            SIZES.put(MAX_KEY, max);

            // 存储
            storeSizeMap();

            return SIZES.get(MAX_KEY);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 查找默认值
     */
    public static final int findOrCreate(String key) throws Exception {
        lock.writeLock().lock();
        try {
            if (SIZES.containsKey(key)) {
                return SIZES.get(key);
            }

            // 最大值
            int max = findMaxValue() + 1;

            // 更新
            SIZES.put(key, max);
            SIZES.put(MAX_KEY, max);

            // 存储
            storeSizeMap();

            return max;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 存储配置文件
     */
    public static final void storeSizeMap() throws Exception {
        lock.writeLock().lock();
        try {
            NapUtils.storeIntMapFile(SIZES, findCfgFile());
        } finally {
            lock.writeLock().unlock();
        }
    }

}
