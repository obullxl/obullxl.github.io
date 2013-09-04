/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.atom.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

/**
 * Aspx分页处理器测试
 * 
 * @author obullxl@gmail.com
 * @version $Id: AspxParserTest.java, 2012-5-19 下午10:24:36 Exp $
 */
public class AspxParserTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // String eventTarget = "";
        // String eventArgument = "";
        String viewState = "";
        String eventValidation = "";

        Parser parser = null;
        // NodeClassFilter tableRow = new NodeClassFilter(TableRow.class);
        // NodeList rows = null;
        HttpConnectionManager connMan = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams par = new HttpConnectionManagerParams();
        connMan.setParams(par);
        HttpClient client = new HttpClient(connMan);
        
        byte[] buffer = null;
        GetMethod get = null;
        PostMethod post = null;
        Charset charset = Charset.forName("utf8");
        CharsetDecoder decoder = charset.newDecoder();

        for (int n = 0; n < 30; n++) {
            if (n == 0) {
                // 开始获取链接,这是第一页
                get = new GetMethod("http://www.it-tender.gov.cn/MoreNewsList2.aspx?id=6");
                client.executeMethod(get);
                InputStream in = get.getResponseBodyAsStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[2048];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                buffer = out.toByteArray();
                ByteBuffer bbuf = ByteBuffer.allocate(buffer.length);
                bbuf.put(buffer);
                bbuf.flip();
                CharBuffer charBuf = decoder.decode(bbuf);
                
                parser = new Parser();
                parser.setInputHTML(charBuf.toString());
                parser.setEncoding("utf8");

                NodeClassFilter divNode = new NodeClassFilter(Div.class);
                HasAttributeFilter attrfilter = new HasAttributeFilter("class", "bigcontent");
                AndFilter andFilter = new AndFilter(divNode, attrfilter);
                NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
                //获取状态值
                parser.reset();
                NodeClassFilter inputNode = new NodeClassFilter(InputTag.class);
                HasAttributeFilter inputattrfilter = new HasAttributeFilter("id", "__VIEWSTATE");
                AndFilter inputandFilter = new AndFilter(inputNode, inputattrfilter);
                NodeList inputList = parser.extractAllNodesThatMatch(inputandFilter);
                InputTag input = (InputTag) inputList.elementAt(0);
                viewState = input.getAttribute("value");

                parser.reset();
                NodeClassFilter inputNode2 = new NodeClassFilter(InputTag.class);
                HasAttributeFilter inputattrfilter2 = new HasAttributeFilter("id", "__EVENTVALIDATION");
                AndFilter inputandFilter2 = new AndFilter(inputNode2, inputattrfilter2);
                NodeList inputList2 = parser.extractAllNodesThatMatch(inputandFilter2);
                InputTag input2 = (InputTag) inputList2.elementAt(0);
                eventValidation = input2.getAttribute("value");

                Parser parser2 = Parser.createParser(nodeList.toHtml(), "utf8");
                NodeClassFilter linkNode = new NodeClassFilter(LinkTag.class);
                NodeList nodeList2 = parser2.extractAllNodesThatMatch(linkNode);
                for (int i = 0; i < nodeList2.size(); i++) {
                    LinkTag link = (LinkTag) nodeList2.elementAt(i);
                    String extractUrl = link.extractLink();
                    if (extractUrl.contains("NewsView.aspx")) {
                        System.out.println(extractUrl);
                    }
                }
            } else {
                // 开始获取链接
                post = new PostMethod("http://www.it-tender.gov.cn/MoreNewsList2.aspx?id=6");
                post.addParameter("__VIEWSTATE", viewState);
                post.addParameter("__EVENTVALIDATION", eventValidation);
                post.addParameter("__EVENTTARGET", "ctl00$cphMain$rptPager$ctl02");
                post.addParameter("__EVENTARGUMENT", "");
                
                client.executeMethod(post);
                InputStream in = post.getResponseBodyAsStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[2048];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                buffer = out.toByteArray();
                ByteBuffer bbuf = ByteBuffer.allocate(buffer.length);
                bbuf.put(buffer);
                bbuf.flip();
                CharBuffer charBuf = decoder.decode(bbuf);
                parser = new Parser();
                parser.setInputHTML(charBuf.toString());
                parser.setEncoding("utf8");
                NodeClassFilter divNode = new NodeClassFilter(Div.class);
                HasAttributeFilter attrfilter = new HasAttributeFilter("class", "bigcontent");
                AndFilter andFilter = new AndFilter(divNode, attrfilter);
                NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
                parser.reset();
                NodeClassFilter inputNode = new NodeClassFilter(InputTag.class);
                HasAttributeFilter inputattrfilter = new HasAttributeFilter("id", "__VIEWSTATE");
                AndFilter inputandFilter = new AndFilter(inputNode, inputattrfilter);
                NodeList inputList = parser.extractAllNodesThatMatch(inputandFilter);
                InputTag input = (InputTag) inputList.elementAt(0);
                viewState = input.getAttribute("value");

                parser.reset();
                NodeClassFilter inputNode2 = new NodeClassFilter(InputTag.class);
                HasAttributeFilter inputattrfilter2 = new HasAttributeFilter("id", "__EVENTVALIDATION");
                AndFilter inputandFilter2 = new AndFilter(inputNode2, inputattrfilter2);
                NodeList inputList2 = parser.extractAllNodesThatMatch(inputandFilter2);
                InputTag input2 = (InputTag) inputList2.elementAt(0);
                eventValidation = input2.getAttribute("value");

                Parser parser2 = Parser.createParser(nodeList.toHtml(), "utf8");
                NodeClassFilter linkNode = new NodeClassFilter(LinkTag.class);
                NodeList nodeList2 = parser2.extractAllNodesThatMatch(linkNode);
                for (int i = 0; i < nodeList2.size(); i++) {
                    LinkTag link = (LinkTag) nodeList2.elementAt(i);
                    String extractUrl = link.extractLink();
                    if (extractUrl.contains("NewsView.aspx")) {
                        System.out.println(extractUrl);
                    }
                }
            }
        }

        get.releaseConnection();
    }
}
