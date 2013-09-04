/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.atom.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
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
import org.htmlparser.util.NodeList;
import org.junit.Test;

/**
 * 表单解析测试
 * 
 * @author obullxl@gmail.com
 * @version $Id: FormParserTest.java, 2012-5-19 下午11:24:53 Exp $
 */
public class FormParserTest {

    @Test
    public void test_form_input() throws Exception {
        String html = FileUtils.readFileToString(new File("F:/Crwal/nid-copy/product-copy.htm"));
        Parser parser = Parser.createParser(html, "UTF-8");

        NodeFilter inputNode = new NodeClassFilter(InputTag.class);
        NodeFilter typeFilter = new HasAttributeFilter("type", "hidden");
        NodeFilter idFilter = new HasAttributeFilter("id", "__VIEWSTATE");
        NodeFilter nameFilter = new HasAttributeFilter("name", "__VIEWSTATE");
        AndFilter viewStateFilter = new AndFilter(new NodeFilter[] { inputNode, typeFilter, idFilter, nameFilter });

        NodeList inputList = parser.extractAllNodesThatMatch(viewStateFilter);
        InputTag input = (InputTag) inputList.elementAt(0);
        String viewState = input.getAttribute("value");

        System.out.println("__VIEWSTATE = " + viewState);
    }

    @Test
    public void test_page_info() throws Exception {
        String html = FileUtils.readFileToString(new File("F:/Crwal/nid-copy/product-copy.htm"));
        Parser parser = Parser.createParser(html, "UTF-8");

        NodeFilter tdNode = new NodeClassFilter(TableColumn.class);
        NodeFilter pageFilter = new HasAttributeFilter("class", "pageinfo");
        NodeFilter alignFilter = new HasAttributeFilter("align", "left");
        NodeFilter valignFilter = new HasAttributeFilter("valign", "bottom");
        NodeFilter nowrapFilter = new HasAttributeFilter("nowrap", "true");
        NodeFilter styleFilter = new HasAttributeFilter("style", "width:;vertical-align:middle;");
        NodeFilter andFilter = new AndFilter(new NodeFilter[] { tdNode, pageFilter, alignFilter, valignFilter, nowrapFilter, styleFilter });

        NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);

        System.out.println(nodeList.elementAt(0).toPlainTextString());
    }

    @Test
    public void test_post_back() throws Exception {
        String html = FileUtils.readFileToString(new File("F:/Crwal/nid-copy/product-copy.htm"));
        Parser parser = Parser.createParser(html, "UTF-8");

        NodeFilter tdNode = new NodeClassFilter(TableColumn.class);
        NodeFilter alignFilter = new HasAttributeFilter("align", "center");
        NodeFilter pageFilter = new HasAttributeFilter("class", "pages");
        NodeFilter valignFilter = new HasAttributeFilter("valign", "bottom");
        NodeFilter nowrapFilter = new HasAttributeFilter("nowrap", "true");
        NodeFilter andFilter = new AndFilter(new NodeFilter[] { tdNode, pageFilter, alignFilter, valignFilter, nowrapFilter });
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
            int start = StringUtils.indexOf(href, "'");
            int end = StringUtils.indexOf(href, "'", start + 1);

            String evtTarget = StringUtils.substring(href, start + 1, end);

            System.out.println(evtTarget + " <+> " + href);
        }
    }

}
