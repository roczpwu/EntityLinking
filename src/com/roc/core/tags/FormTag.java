package com.roc.core.tags;

import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.roc.core.Utils.NumberUtil;
import com.roc.core.Utils.StringUtil;
import com.roc.core.base.BaseAction;
import com.roc.core.jqueryValidation.Validation;
import org.apache.struts2.dispatcher.Dispatcher;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: 文本框标签类
 */
public class FormTag extends BaseTag {

    private String action;          // 表单action名
    private String formId = null;   // 表单id
    private String method = "get";

    @Override
    public int doStartTag() throws JspException {
        System.out.println("");
        if (this.formId == null)
            this.formId= "form-"+NumberUtil.generateUUID();
        this.getFormToActionMapFrom().getForm2action().put(this.formId, this.action);
        JspWriter out=pageContext.getOut();
        try {
            out.println("<form id='"+this.formId+"' action="+this.action+" method='"+this.method+"'>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out=pageContext.getOut();
        try {
            out.println("</form>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (StringUtil.isEmpty(action)) {
            this.action = null;
            return;
        }
        while (action.contains("//")) {
            action = action.replace("//", "/");
        }
        this.action = action;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
