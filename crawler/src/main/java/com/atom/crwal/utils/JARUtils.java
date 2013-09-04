/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * JAR包工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: JARUtils.java, 2012-5-20 下午10:36:54 Exp $
 */
public class JARUtils {

    /** URLClassLoader的addURL方法 */
    private static Method addURL = initAddMethod();

    /** 初始化方法 */
    private static final Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();

    /** 
     * 循环遍历目录，找出所有的JAR包 
     */
    private static final void loopFiles(File file, List<File> files) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp, files);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                files.add(file);
            }
        }
    }

    /** 
     * <pre> 
     * 加载JAR文件 
     * </pre> 
     * 
     * @param file 
     */
    public static final void loadJarFile(File file) {
        try {
            addURL.invoke(system, new Object[] { file.toURI().toURL() });
            System.out.println("加载JAR包：" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * <pre> 
     * 从一个目录加载所有JAR文件 
     * </pre> 
     * 
     * @param path 
     */
    public static final void loadJarPath(String path) {
        File lib = new File(path);
        if(!lib.exists()) {
            return;
        }
        
        List<File> files = new ArrayList<File>();
        loopFiles(lib, files);
        for (File file : files) {
            loadJarFile(file);
        }
    }

}
