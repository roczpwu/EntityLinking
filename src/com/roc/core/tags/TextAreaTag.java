package com.roc.core.tags;

import com.roc.core.Utils.NumberUtil;
import com.roc.core.Utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Created by rocwu on 2016/10/25.
 */
public class TextAreaTag extends BaseTag {

    public String textAreaId = null;

    private String name;

    private String value = "";

    private String label;

    private int maxLength = Integer.MAX_VALUE;

    @Override
    public int doStartTag() throws JspException {
        if (textAreaId == null)
            textAreaId = NumberUtil.generateUUID();
        if (StringUtil.isEmpty(this.label))
            this.label = "";
        JspWriter out=pageContext.getOut();
        try {
            out.println("<div class=\"form-group\">");
            if (!StringUtil.isEmpty(this.label))
                out.println("\t<label class=\"control-label\" for=\""+textAreaId+"\">"+this.label+"</label>");
            out.print("\t<textarea id=\""+textAreaId+"\" class=\"form-control\" name=\""+this.name+"\" maxlength=\""+this.maxLength+"\">");
            if (this.value != null) out.print(this.value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out=pageContext.getOut();
        try {
            out.println("</textarea>");
            out.println("</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getTextAreaId() {
        return textAreaId;
    }

    public void setTextAreaId(String textAreaId) {
        this.textAreaId = textAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
