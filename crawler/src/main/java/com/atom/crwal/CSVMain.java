/**
 * Author: obullxl@gmail.com
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.atom.crwal;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * CSV文件解析器
 * 
 * @author obullxl@gmail.com
 * @version $Id: CSVMain.java, V1.0.1 2013-9-15 下午2:24:36 $
 */
public class CSVMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String file = "";

        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "GBK"));

            String[] values = reader.readNext();
            while (values != null) {

                // 下一行
                values = reader.readNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

}
