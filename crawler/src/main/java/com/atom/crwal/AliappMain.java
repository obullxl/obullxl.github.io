/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.atom.crwal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atom.crwal.utils.ConfigUtils;
import com.atom.crwal.utils.DBUtils;
import com.atom.crwal.utils.HtmlUtils;

/**
 * 阿里云引擎Web抓取
 * 
 * @author shizihu
 * @version $Id: AliappMain.java, v 0.1 2013-9-4 上午09:18:23 shizihu Exp $
 */
public class AliappMain {
    private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        try {
            DBUtils.initDataSource();
            fetchCatgs();
            fetchTopics();
        } finally {
            DBUtils.closeDataSource();
        }
    }

    /**
     * 获取导航页面
     */
    private static void fetchCatgs() throws Exception {
        HtmlUtils.saveHTML("index.html");

        List<String> catgs = Arrays.asList("blog", "news", "misc", "album", "about");
        for (String catg : catgs) {
            HtmlUtils.saveHTML("index-" + catg + ".html");
        }
    }

    /**
     * 获取详情页面
     */
    private static void fetchTopics() throws Exception {
        String sql = "SELECT id, catg, title FROM atom_topic WHERE id>? AND id<=? ORDER BY id ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.fetchConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ConfigUtils.findMinID());
            pstmt.setLong(2, ConfigUtils.findMaxID());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String catg = rs.getString("catg");
                String title = rs.getString("title");
                logger.warn("主题信息-{},{},{}", id, catg, title);

                String fname = "topic-" + catg + "-" + id + ".html";
                HtmlUtils.saveHTML(fname + "?v=clean", fname);
            }
        } finally {
            DBUtils.closeQuietly(rs);
            DBUtils.closeQuietly(pstmt);
            DBUtils.freeConnection();
        }
    }

}
