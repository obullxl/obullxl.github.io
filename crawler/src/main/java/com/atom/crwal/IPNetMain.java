/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.atom.crwal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.atom.core.lang.utils.DateUtils;

/**
 * 网络IP信息
 * 
 * @author shizihu
 * @version $Id: IPNetMain.java, v 0.1 2013-9-14 下午04:05:20 shizihu Exp $
 */
public class IPNetMain {

    /**
     * 定时把IP信息写入文件中
     */
    public static void main(String[] args) {
        Date now = new Date();
        Timer timer = new Timer("IPNetMain", true);

        // 1个小时检查一次是否过期
        timer.schedule(new TimerTask() {
            public void run() {
                execute();
            }
        }, now, 60 * 60 * 1000);

        // 防止程序退出
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }.start();
    }

    /**
     * 获取IP信息并写入文件
     */
    private static void execute() {
        OutputStream output = null;
        InputStream input = null;
        try {
            Process process = Runtime.getRuntime().exec("ipconfig /all");

            String path = "d:/Data/BDCloud/CodeSpace/ipnet";
            FileUtils.forceMkdir(new File(path));

            String fname = DateUtils.toString(new Date(), "yyyyMMdd-HH-mm-ss") + ".txt";
            output = new FileOutputStream(new File(FilenameUtils.normalize(path + "/" + fname)),
                true);
            output.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n".getBytes());
            output.write(DateUtils.toStringDL(new Date()).getBytes());
            output.write("\n".getBytes());
            output.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n".getBytes());

            input = new BufferedInputStream(process.getInputStream());

            IOUtils.copy(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

}
