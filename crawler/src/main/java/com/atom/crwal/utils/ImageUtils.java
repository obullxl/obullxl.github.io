/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * 图片工具类
 * 
 * @author shizihu
 * @version $Id: ImageUtils.java, v 0.1 2012-6-1 下午2:01:15 shizihu Exp $
 */
public class ImageUtils {
    /** 读写锁 */
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 抓取图片保存到本地，每个文件夹5000个图片
     */
    public static final String fetchImage(String id, String url) throws Exception {
        lock.writeLock().lock();
        try {
            // 日期目录
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String dpath = FilenameUtils.normalize(Consts.ROOT + "/images/" + date);
            File dfile = new File(dpath);
            FileUtils.forceMkdir(dfile);

            // 最后子目录
            String subpath = "100001";

            String[] subpaths = dfile.list();
            if (subpaths.length > 0) {
                Arrays.sort(subpaths);
                String tmpath = subpaths[subpaths.length - 1];
                File subfile = new File(FilenameUtils.normalize(dpath + "/" + tmpath));

                if (subfile.list().length < 10) {
                    subpath = tmpath;
                } else {
                    subpath = Integer.toString(Integer.parseInt(tmpath) + 1);
                }
            }

            // 文件目录
            File fpath = new File(FilenameUtils.normalize(dpath + "/" + subpath));
            FileUtils.forceMkdir(fpath);

            // 文件名
            String fname = id + "." + FilenameUtils.getExtension(url);

            // 复制内容
            OutputStream out = new FileOutputStream(new File(fpath, fname));

            GetMethod get = new GetMethod(url);
            HttpUtils.findHttpClient().executeMethod(get);

            IOUtils.copy(get.getResponseBodyAsStream(), out);
            IOUtils.closeQuietly(out);

            // 返回值：20120520/10002/1.jpg
            return date + "/" + subpath + "/" + fname;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
