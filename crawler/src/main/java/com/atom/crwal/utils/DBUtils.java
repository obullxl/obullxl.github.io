/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;

import com.atom.crwal.utils.ConfigUtils;

/**
 * DB工具类
 * 
 * @author shizihu
 * @version $Id: DBUtils.java, v 0.1 2012-6-8 下午03:24:24 shizihu Exp $
 */
public class DBUtils {
    /** 当前线程打开的连接 */
    private static final ThreadLocal<Connection> CONN = new ThreadLocal<Connection>();

    /** 数据源 */
    private static BasicDataSource               DS   = null;

    /**
     * 初始化数据源
     */
    public static final void initDataSource() {
        DS = new BasicDataSource();

        DS.setDriverClassName(ConfigUtils.findConfig("jdbc.driver"));
        DS.setUrl(ConfigUtils.findConfig("jdbc.url"));
        DS.setUsername(ConfigUtils.findConfig("jdbc.user"));
        DS.setPassword(ConfigUtils.findConfig("jdbc.passwd"));
        DS.setMaxActive(5);
    }

    /**
     * 获取数据库连接
     */
    public static final Connection fetchConnection() {
        Connection conn = CONN.get();

        if (conn == null) {
            try {
                conn = DS.getConnection();
            } catch (Exception e) {
                throw new RuntimeException("从数据源中取出连接异常！", e);
            }

            CONN.set(conn);
        }

        return conn;
    }

    /**
     * 释放数据库连接
     */
    public static final void freeConnection() {
        Connection conn = CONN.get();

        if (conn != null) {
            try {
                CONN.remove();
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException("释放数据库连接异常！", e);
            }
        }
    }

    /**
     * 关闭数据源
     */
    public static final void closeDataSource() {
        try {
            DS.close();
        } catch (Exception e) {
            throw new RuntimeException("关闭数据源异常！", e);
        }
    }

    /**
     * 关闭Statement
     */
    public static final void closeQuietly(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 关闭ResultSet
     */
    public static final void closeQuietly(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            // ignore
        }
    }

}
