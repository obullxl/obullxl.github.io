/**
 * aBoy.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.crwal.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * POST表单信息
 * 
 * @author obullxl@gmail.com
 * @version $Id: PostFormDTO.java, 2012-5-20 下午12:15:40 Exp $
 */
public class PostFormDTO {

    private String action;

    private String eventTarget;
    private String eventArgument;

    private String viewState;
    private String viewStateEncrypted;

    private String previousPage;
    private String eventValidation;

    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEventTarget() {
        return eventTarget;
    }

    public void setEventTarget(String eventTarget) {
        this.eventTarget = eventTarget;
    }

    public String getEventArgument() {
        return eventArgument;
    }

    public void setEventArgument(String eventArgument) {
        this.eventArgument = eventArgument;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getViewStateEncrypted() {
        return viewStateEncrypted;
    }

    public void setViewStateEncrypted(String viewStateEncrypted) {
        this.viewStateEncrypted = viewStateEncrypted;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getEventValidation() {
        return eventValidation;
    }

    public void setEventValidation(String eventValidation) {
        this.eventValidation = eventValidation;
    }

}
