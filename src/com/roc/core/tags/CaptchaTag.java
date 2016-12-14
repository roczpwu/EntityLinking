package com.roc.core.tags;

import com.roc.core.Utils.NumberUtil;
import com.roc.core.Utils.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Random;

/**
 * User: rocwu
 * Date: 2016/09/02
 * Time: 19:51
 * Desc: 验证码标签类
 */
public class CaptchaTag extends BaseTag {

    private String name;

    private String value = "";

    private String label;

    @Override
    public int doEndTag() throws JspException {
        String inputId = NumberUtil.generateUUID();
        if (StringUtil.isEmpty(this.label))
            this.label = this.name;
        JspWriter out=pageContext.getOut();
        try {
            out.println("<div class=\"form-group\">");
            out.println("\t<label class=\"control-label\" for=\""+inputId+"\">"+this.label+"</label>");
            Random random = new Random(System.currentTimeMillis());
            int v = random.nextInt(1000);
            out.println("\t<img name=\""+this.name+"-img\" id=\""+inputId+"-image\" src=\"/captcha.action?v="+v+"&name="+this.name+"\">");
            out.println("\t<input name=\""+this.name+"-v\" type=\"hidden\" value=\""+v+"\"/>");
            out.println("\t<input name=\""+this.name+"\" type=\"text\" id=\""+inputId+"\" class=\"form-control\"/>");
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
}
