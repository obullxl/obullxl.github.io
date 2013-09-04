/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.dto;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.atom.crwal.utils.SizeUtils;

/**
 * 产品信息
 *
 * http://www.ecdrops.com/product/BrandProduct.aspx?bid=6&tid=1392
 *
bid=6,
tid=1392,
nid=,
v_products_model=A&amp;F MC:889,
v_products_image=http://174.37.190.85:8888/uploadimage/20101013/7d1aa635-24e4-4e93-94c9-078551a06a65.jpg,
v_products_name_1=A&amp;F MC:889,
v_products_price=23.60,
v_products_weight=1.2
S M L XL,
v_products_quantity=100
 *
 * @author obullxl@gmail.com
 * @version $Id: ProductDTO.java, 2012-5-20 下午4:38:15 Exp $
 */
public class ProductDTO {

    public String              id;

    public String              bid;
    public String              catg_name_1;

    public String              tid;
    public String              catg_name_2;

    public String              nid;
    public String              catg_name_3;

    public String              v_image;
    public String              v_name_1;

    public String[]            v_sizes;

    public String              v_price;
    public String              v_weight;
    public String              v_quantity;

    public static final String PRD_TITLE = "v_products_model,v_products_image,v_products_name_2,v_products_description_2,v_products_url_2,v_products_name_1,v_products_description_1,v_products_url_1,v_specials_price,v_specials_last_modified,v_specials_expires_date,v_products_price,v_products_weight,v_last_modified,v_date_added,v_products_quantity,v_manufacturers_name,v_categories_name_1,v_categories_name_2,v_categories_name_3,v_categories_name_4,v_categories_name_5,v_categories_name_6,v_categories_name_7,v_tax_class_title,v_status,v_metatags_products_name_status,v_metatags_title_status,v_metatags_model_status,v_metatags_price_status,v_metatags_title_tagline_status,v_metatags_title_2,v_metatags_keywords_2,v_metatags_description_2,v_metatags_title_1,v_metatags_keywords_1,v_metatags_description_1";
    public static final String ATR_TITLE = "v_products_model,v_attribute_options_id_1,v_attribute_options_name_1_1,v_attribute_values_id_1_1,v_attribute_values_price_1_1,v_attribute_values_name_1_1_1,v_attribute_values_id_1_2,v_attribute_values_price_1_2,v_attribute_values_name_1_2_1,v_attribute_values_id_1_3,v_attribute_values_price_1_3,v_attribute_values_name_1_3_1,v_attribute_values_id_1_4,v_attribute_values_price_1_4,v_attribute_values_name_1_4_1,v_attribute_values_id_1_5,v_attribute_values_price_1_5,v_attribute_values_name_1_5_1,v_attribute_values_id_1_6,v_attribute_values_price_1_6,v_attribute_values_name_1_6_1,v_attribute_values_id_1_7,v_attribute_values_price_1_7,v_attribute_values_name_1_7_1,v_attribute_values_id_1_8,v_attribute_values_price_1_8,v_attribute_values_name_1_8_1,v_attribute_values_id_1_9,v_attribute_values_price_1_9,v_attribute_values_name_1_9_1";

    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 转化为CSV文件（产品信息，属性信息）
     */
    public final String[] toCSV() throws Exception {
        // 产品
        StringBuilder prdTxt = new StringBuilder(256);
        // v_products_model,v_products_image
        prdTxt.append(id).append(",").append(this.v_image);
        // v_products_name_2,v_products_description_2,v_products_url_2
        prdTxt.append(",,,");
        // v_products_name_1
        prdTxt.append(",").append(StringUtils.trimToEmpty(this.v_name_1));
        // v_products_description_1,v_products_url_1,v_specials_price,v_specials_last_modified,v_specials_expires_date
        prdTxt.append(",,,,,");
        // v_products_price,v_products_weight
        prdTxt.append(",").append(this.v_price).append(",").append(this.v_weight);
        // v_last_modified,v_date_added
        prdTxt.append(",,");
        // v_products_quantity
        prdTxt.append(",").append(this.v_quantity);
        // v_manufacturers_name
        prdTxt.append(",");
        // v_categories_name_1,v_categories_name_2,v_categories_name_3
        prdTxt.append(",").append(StringUtils.trimToEmpty(catg_name_1));
        prdTxt.append(",").append(StringUtils.trimToEmpty(catg_name_2));
        prdTxt.append(",").append(StringUtils.trimToEmpty(catg_name_3));

        // v_categories_name_4,,v_categories_name_6,v_categories_name_7
        // v_tax_class_title,v_status,v_metatags_products_name_status,v_metatags_title_status
        // v_metatags_model_status,v_metatags_price_status,v_metatags_title_tagline_status,
        // v_metatags_title_2,v_metatags_keywords_2,v_metatags_description_2,v_metatags_title_1
        // v_metatags_keywords_1,v_metatags_description_1
        prdTxt.append(",,,,,,,,,,,,,,,,,");

        // 属性
        String attributs = StringUtils.EMPTY;
        if (this.v_sizes != null && this.v_sizes.length >= 1) {
            StringBuilder atrTxt = new StringBuilder(256);
            // v_products_model,v_attribute_options_id_1,v_attribute_options_name_1_1
            atrTxt.append(id).append(",1,Size");
            // 27个属性
            for (String size : this.v_sizes) {
                atrTxt.append(",").append(SizeUtils.findOrCreate(size)).append(",0,").append(size);
            }
            // 后面加逗号
            int count = (27 - 1) - this.v_sizes.length;
            for (int i = 0; i < count; i++) {
                atrTxt.append(",");
            }

            // CSV内容
            attributs = atrTxt.toString();
        }

        // 返回
        return new String[] { prdTxt.toString(), attributs };
    }
}
