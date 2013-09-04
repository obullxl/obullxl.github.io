/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

/**
 * 常量
 * 
 * @author shizihu
 * @version $Id: Consts.java, v 0.1 2012-6-25 下午9:48:46 shizihu Exp $
 */
public class Consts {

    /** 系统启动目录 */
    public static String ROOT = FilenameUtils.normalizeNoEndSeparator(new File(".")
                                  .getAbsolutePath());

}
