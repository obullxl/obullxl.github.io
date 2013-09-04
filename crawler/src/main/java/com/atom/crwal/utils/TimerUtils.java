/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.util.Date;
import java.util.Timer;

import org.apache.commons.lang.time.DateUtils;

import com.atom.crwal.task.CheckCopyrightTask;

/**
 * Time工具类
 * 
 * @author shizihu
 * @version $Id: TimerUtils.java, v 0.1 2012-6-19 下午1:00:34 shizihu Exp $
 */
public class TimerUtils {

    /** 定时器 */
    private static Timer timer = null;

    /**
     * 初始化
     */
    public static final void initTimer() {
        if (timer != null) {
            return;
        }

        // 定时器
        timer = new Timer("WebCrwalTimer", true);

        // 初始化任务
        initTasks();
    }

    /**
     * 初始化任务
     */
    private static final void initTasks() {
        // 5分钟检查一次是否过期
        findTimer().schedule(new CheckCopyrightTask(), new Date(), 5 * DateUtils.MILLIS_PER_MINUTE);
    }

    /**
     * 获取定时器
     */
    public static final Timer findTimer() {
        if (timer == null) {
            initTimer();
        }

        return timer;
    }

}
