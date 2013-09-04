/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.task;

import java.util.Date;
import java.util.TimerTask;

import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查系统版权定时器任务
 * 
 * @author obullxl@gmail.com
 * @version $Id: CheckCopyrightTask.java, 2012-6-22 下午5:58:16 Exp $
 */
public class CheckCopyrightTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    /**
     * @see java.util.TimerTask#run()
     */
    public void run() {
        // 检查系统
        try {
            Date now = new Date();
            Date date = DateUtils.parseDate("2015-07-20", new String[] { "yyyy-MM-dd" });
            if (now.after(date)) {
                logger.error("###################################################");
                logger.error("#### 您目前使用的本系统版本为测试版本，请尽快升级系统到最终发布版本！！！");
                logger.error("###################################################");
                System.exit(-1);
            }
        } catch (Exception e) {
            logger.error("定时器检查系统版权异常！", e);
        }
    }

}
