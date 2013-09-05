/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.atom.crwal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.atom.core.lang.utils.DateUtils;
import com.atom.core.lang.xml.XMLNode;
import com.atom.core.lang.xml.XMLUtils;
import com.atom.crwal.utils.Consts;
import com.atom.crwal.utils.DBUtils;

/**
 * 博客园
 * 
 * @author shizihu
 * @version $Id: CnblogMain.java, v 0.1 2013-9-5 上午09:11:19 shizihu Exp $
 */
public class CnblogMain {
    // private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String file = FilenameUtils.normalize(Consts.ROOT + "/rss-cnblog.xml");
        XMLNode root = XMLUtils.toXMLNode(file);

        DBUtils.initDataSource();
        try {
            for (XMLNode node : root.getChildren()) {
                if (StringUtils.equalsIgnoreCase(node.getName(), "entry")) {
                    processEntry(node);
                }
            }
        } finally {
            DBUtils.freeConnection();
            DBUtils.closeDataSource();
        }
    }

    /**
     * 处理单个节点
     */
    private static void processEntry(XMLNode entry) throws Exception {
        String title = findChild(entry, "title").getText();
        String summary = findChild(entry, "summary").getText();
        String update = StringUtils.substring(findChild(entry, "updated").getText(), 0, 10);
        String content = findChild(entry, "content").getText();

        Map<String, String> data = new HashMap<String, String>();
        data.put("catg", "blog");
        data.put("title", title);
        data.put("summary", summary);
        data.put("content", content);
        data.put("update", update);

        saveData(data);
    }

    /**
     * 获取单个指定子节点
     */
    private static XMLNode findChild(XMLNode entry, String name) {
        for (XMLNode child : entry.getChildren()) {
            if (StringUtils.equalsIgnoreCase(child.getName(), name)) {
                return child;
            }
        }

        return null;
    }

    /**
     * 保存数据
     */
    private static void saveData(Map<String, String> data) throws Exception {
        String sql = "INSERT INTO atom_topic(catg, title, summary, content, gmt_create, gmt_modify) VALUES(?, ?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.fetchConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.get("catg"));
            pstmt.setString(2, substring(data.get("title"), 255));
            pstmt.setString(3, substring(data.get("summary"), 255));
            pstmt.setString(4, data.get("content"));
            pstmt.setString(5, DateUtils.toStringDL(DateUtils.toDateDW(data.get("update"))));

            pstmt.executeUpdate();
        } finally {
            DBUtils.closeQuietly(pstmt);
        }
    }

    /**
     * 字符串截取
     */
    private static String substring(String text, int length) throws Exception {
        if (StringUtils.isEmpty(text) || length < 0) {
            return text;
        }
        
        if(text.getBytes("GBK").length <= length) {
            return text;
        }

        StringBuilder txt = new StringBuilder();
        
        int bytes = 0;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            byte[] b = s.getBytes("GBK");

            if ((bytes + b.length) > length) {
                break;
            }
            
            bytes += b.length;
            txt.append(chars[i]);
        }

        return txt.toString();
    }

}
