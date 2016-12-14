package com.roc.core.tags;

import com.roc.core.Utils.NumberUtil;
import com.roc.core.Utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * User: rocwu
 * Date: 2016/09/02
 * Time: 19:51
 * Desc: 输入域标签类
 */
public class InputTag extends BaseTag {

    private String name;

    private String value = "";

    private String label;

    private String type = "text";

    @Override
    public int doEndTag() throws JspException {
        String inputId = NumberUtil.generateUUID();
        if (StringUtil.isEmpty(this.label))
            this.label = "";
        JspWriter out=pageContext.getOut();
        try {
            out.println("<div class=\"form-group\">");
            if (!StringUtil.isEmpty(this.label))
                out.println("\t<label class=\"control-label\" for=\""+inputId+"\">"+this.label+"</label>");
            out.println("\t<input type=\""+this.type+"\" id=\""+inputId+"\" class=\"form-control\" name=\""+this.name+"\" value=\""+this.value+"\"/>");
            out.println("</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
