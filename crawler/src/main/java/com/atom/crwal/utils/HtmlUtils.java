/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atom.crwal.dto.PostFormDTO;

/**
 * HTML工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: HtmlUtils.java, 2012-5-20 下午12:25:58 Exp $
 */
public class HtmlUtils {
    private static final Logger logger = LoggerFactory.getLogger("LOGGER");

    /**
     * 获取总页数
     */
    public static final int findPageCount(String url, String html) {
        Parser parser = Parser.createParser(html, "UTF-8");

        NodeFilter tdNode = new NodeClassFilter(TableColumn.class);
        NodeFilter pageFilter = new HasAttributeFilter("class", "pageinfo");
        NodeFilter alignFilter = new HasAttributeFilter("align", "left");
        NodeFilter valignFilter = new HasAttributeFilter("valign", "bottom");
        NodeFilter nowrapFilter = new HasAttributeFilter("nowrap", "true");
        NodeFilter styleFilter = new HasAttributeFilter("style", "width:;vertical-align:middle;");
        NodeFilter andFilter = new AndFilter(new NodeFilter[] { tdNode, pageFilter, alignFilter,
                valignFilter, nowrapFilter, styleFilter });

        try {
            NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
            if (nodeList.size() <= 0) {
                return -1;
            }

            // Page: 1 of 7
            String text = nodeList.elementAt(0).toPlainTextString();
            String[] splits = StringUtils.split(text, "of");
            String pages = StringUtils.trimToEmpty(splits[splits.length - 1]);

            logger.info("产品地址[" + url + "]的分页信息为[" + text + "]，总页数[" + pages + "]..");

            return Integer.parseInt(pages);
        } catch (Exception e) {
            logger.error("产品地址[" + url + "]解析分页信息异常！", e);
        }

        // 默认没有
        return -1;
    }

    /**
     * 解析产品表单信息
     */
    public static final PostFormDTO toFormDTO(String html, String url, int page) throws Exception {
        // 表单
        PostFormDTO form = new PostFormDTO();
        form.setAction(url);

        // 解析器
        Parser parser = Parser.createParser(html, "UTF-8");

        // __EVENTTARGET
        NodeFilter tdNode = new NodeClassFilter(TableColumn.class);
        NodeFilter alignFilter = new HasAttributeFilter("align", "center");
        NodeFilter pageFilter = new HasAttributeFilter("class", "pages");
        NodeFilter valignFilter = new HasAttributeFilter("valign", "bottom");
        NodeFilter nowrapFilter = new HasAttributeFilter("nowrap", "true");
        NodeFilter andFilter = new AndFilter(new NodeFilter[] { tdNode, pageFilter, alignFilter,
                valignFilter, nowrapFilter });
        NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
        String content = nodeList.elementAt(0).toHtml();
        parser.reset();

        parser.setInputHTML(content);
        parser.setEncoding("UTF-8");
        NodeFilter linkNode = new NodeClassFilter(LinkTag.class);
        nodeList = parser.extractAllNodesThatMatch(linkNode);
        Node[] nodes = nodeList.toNodeArray();
        for (Node node : nodes) {
            String href = ((TagNode) node).getAttribute("href");
            if (!StringUtils.startsWith(href, "javascript:__doPostBack")) {
                continue;
            }

            int start = StringUtils.indexOf(href, "'");
            int end = StringUtils.indexOf(href, "'", start + 1);

            form.setEventTarget(StringUtils.substring(href, start + 1, end));
            break;
        }

        // __EVENTARGUMENT
        form.setEventArgument(Integer.toString(page));
        parser.reset();

        // __VIEWSTATE
        parser.setInputHTML(html);
        parser.setEncoding("UTF-8");
        NodeFilter inputNode = new NodeClassFilter(InputTag.class);
        NodeFilter typeFilter = new HasAttributeFilter("type", "hidden");
        NodeFilter idFilter = new HasAttributeFilter("id", "__VIEWSTATE");
        NodeFilter nameFilter = new HasAttributeFilter("name", "__VIEWSTATE");
        AndFilter viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter,
                idFilter, nameFilter });
        NodeList inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setViewState(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __VIEWSTATEENCRYPTED
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__VIEWSTATEENCRYPTED");
        nameFilter = new HasAttributeFilter("name", "__VIEWSTATEENCRYPTED");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setViewStateEncrypted(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __PREVIOUSPAGE
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__PREVIOUSPAGE");
        nameFilter = new HasAttributeFilter("name", "__PREVIOUSPAGE");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setPreviousPage(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __EVENTVALIDATION
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__EVENTVALIDATION");
        nameFilter = new HasAttributeFilter("name", "__EVENTVALIDATION");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setEventValidation(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // 返回
        return form;
    }

    /**
     * 解析更新表单信息
     */
    public static final PostFormDTO toUpdateFormDTO(String html, String url, int page)
                                                                                      throws Exception {
        // 表单
        PostFormDTO form = new PostFormDTO();
        form.setAction(url);

        // 解析表格信息
        Parser parser = Parser.createParser(html, "UTF-8");
        NodeFilter tableNode = new NodeClassFilter(TableTag.class);
        NodeFilter clzFilter = new HasAttributeFilter("class", "newlist");

        NodeFilter andFilter = new AndFilter(new NodeFilter[] { tableNode, clzFilter });
        NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
        String content = nodeList.elementAt(0).toHtml();
        parser.reset();

        // 解析分页表格
        parser.setInputHTML(content);
        parser.setEncoding("UTF-8");

        NodeFilter trNode = new NodeClassFilter(TableTag.class);
        NodeFilter atFilter = new HasAttributeFilter("border", "0");

        andFilter = new AndFilter(new NodeFilter[] { trNode, atFilter });
        nodeList = parser.extractAllNodesThatMatch(andFilter);
        content = nodeList.elementAt(0).toHtml();
        parser.reset();

        // 解析分页链接
        parser.setInputHTML(content);
        parser.setEncoding("UTF-8");

        NodeFilter aNode = new NodeClassFilter(LinkTag.class);
        nodeList = parser.extractAllNodesThatMatch(aNode);
        Node[] nodes = nodeList.toNodeArray();
        for (Node node : nodes) {
            String href = ((TagNode) node).getAttribute("href");
            if (!StringUtils.startsWith(href, "javascript:__doPostBack")) {
                continue;
            }

            int start = StringUtils.indexOf(href, "'");
            int end = StringUtils.indexOf(href, "'", start + 1);

            form.setEventTarget(StringUtils.substring(href, start + 1, end));
            break;
        }

        // __EVENTARGUMENT
        form.setEventArgument("Page$" + Integer.toString(page));
        parser.reset();

        // __VIEWSTATE
        parser.setInputHTML(html);
        parser.setEncoding("UTF-8");
        NodeFilter inputNode = new NodeClassFilter(InputTag.class);
        NodeFilter typeFilter = new HasAttributeFilter("type", "hidden");
        NodeFilter idFilter = new HasAttributeFilter("id", "__VIEWSTATE");
        NodeFilter nameFilter = new HasAttributeFilter("name", "__VIEWSTATE");
        AndFilter viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter,
                idFilter, nameFilter });
        NodeList inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setViewState(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __VIEWSTATEENCRYPTED
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__VIEWSTATEENCRYPTED");
        nameFilter = new HasAttributeFilter("name", "__VIEWSTATEENCRYPTED");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setViewStateEncrypted(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __PREVIOUSPAGE
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__PREVIOUSPAGE");
        nameFilter = new HasAttributeFilter("name", "__PREVIOUSPAGE");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setPreviousPage(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // __EVENTVALIDATION
        inputNode = new NodeClassFilter(InputTag.class);
        typeFilter = new HasAttributeFilter("type", "hidden");
        idFilter = new HasAttributeFilter("id", "__EVENTVALIDATION");
        nameFilter = new HasAttributeFilter("name", "__EVENTVALIDATION");
        viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter,
                nameFilter });
        inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        form.setEventValidation(((InputTag) inputList.elementAt(0)).getAttribute("value"));
        parser.reset();

        // 返回
        return form;
    }

    /**
     * 获取POST表单方法
     */
    public static final PostMethod toPostMethod(PostFormDTO form) {
        PostMethod post = new PostMethod(form.getAction());

        post.addParameter("__EVENTTARGET", form.getEventTarget());
        post.addParameter("__EVENTARGUMENT", form.getEventArgument());

        post.addParameter("__VIEWSTATE", form.getViewState());
        post.addParameter("__VIEWSTATEENCRYPTED", form.getViewStateEncrypted());

        post.addParameter("__PREVIOUSPAGE", form.getPreviousPage());
        post.addParameter("__EVENTVALIDATION", form.getEventValidation());

        return post;
    }

    /**
     * 保存HTML内容
     */
    public static final void saveHTML(String uri) throws Exception {
        saveHTML(uri, uri);
    }

    /**
     * 保存HTML内容
     */
    public static final void saveHTML(String uri, String fname) throws Exception {
        Reader is = null;
        OutputStream out = null;
        try {
            String path = FilenameUtils.normalize(Consts.ROOT);
            FileUtils.forceMkdir(new File(path));
            File config = new File(path).getParentFile();

            String host = ConfigUtils.findHost();
            String html = fetchHtml(host + "/" + uri);
            
            // 处理HMTL内容
            html = processHtml(html);

            is = new StringReader(html);
            out = new FileOutputStream(new File(config, fname));
            IOUtils.copy(is, out, "UTF-8");
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 根据请求的URL获取GET HTML内容
     */
    public static final String fetchHtml(String url) throws Exception {
        // 代理的设置
        GetMethod getMethod = new GetMethod(url);

        getMethod.addRequestHeader("Host", "http://www.baidu.com");

        getMethod
            .addRequestHeader("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.17) Gecko/2009122116 Firefox/3.0.17");
        getMethod.addRequestHeader("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        getMethod.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");

        getMethod.addRequestHeader("Accept-Charset", "gb2312,UTF-8;q=0.7,*;q=0.7");
        getMethod.addRequestHeader("Keep-Alive", "300");
        getMethod.addRequestHeader("Cache-Control", "max-age=0");

        return HttpUtils.get(getMethod);
    }
    
    /**
     * 处理HTML内容
     */
    private static final String processHtml(String html) {
        html = StringUtils.replace(html, "http://anode.aliapp.com/js", "http://obullxl.github.io/public/js");
        html = StringUtils.replace(html, "http://anode.aliapp.com/css", "http://obullxl.github.io/public/css");
        html = StringUtils.replace(html, "http://anode.aliapp.com/img", "http://obullxl.github.io/public/img");
        
        return html;
    }

}
