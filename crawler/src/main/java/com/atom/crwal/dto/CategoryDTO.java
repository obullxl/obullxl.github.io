/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 分类信息
 * 
 * @author obullxl@gmail.com
 * @version $Id: CategoryDTO.java, 2012-5-23 下午9:35:36 Exp $
 */
public class CategoryDTO {

    public String bid;
    public String name_1;

    public String tid;
    public String name_2;

    public String nid;
    public String name_3;
    
    /**
     * CTOR
     */
    public CategoryDTO(String bid, String tid, String nid) {
        this.bid = bid;
        this.tid = tid;
        this.nid = nid;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
