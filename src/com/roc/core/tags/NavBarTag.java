package com.roc.core.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: 导航条标签类
 */
public class NavBarTag extends TagSupport {

    /**
     * 首页标题
     */
    private String homeTitle;

    /**
     * 首页url
     */
    private String homeUrl ="#";

    private Map<String, String> leftNavMap;
    private Map<String, String> rightNavMap;

    /**
     * nav的class属性
     */
    private String className="navbar navbar-inverse navbar-custom navbar-fixed-top";

    @Override
    public int doStartTag() throws JspException {

        return SKIP_BODY;  //不实现标签的体，即空体标签。
    }

    @Override
    public int doEndTag() throws JspException {
        if (leftNavMap==null)leftNavMap = new LinkedHashMap<String, String>();
        if (rightNavMap==null)rightNavMap = new LinkedHashMap<String, String>();
        JspWriter out=pageContext.getOut();
        try {
            out.println("<nav class=\""+className+"\">");
            out.println("\t<div class=\"container-fluid\">");
            out.println("\t\t<!-- Brand and toggle get grouped for better mobile display -->");
            out.println("\t\t<div class=\"navbar-header page-scroll\">");
            out.println("\t\t\t<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\">");
            out.println("\t\t\t\t<span class=\"sr-only\">Toggle navigation</span>");
            out.println("\t\t\t\t<span class=\"icon-bar\"></span>");
            out.println("\t\t\t\t<span class=\"icon-bar\"></span>");
            out.println("\t\t\t\t<span class=\"icon-bar\"></span>");
            out.println("\t\t\t</button>");
            out.println("\t\t\t<a class=\"navbar-brand\" href=\""+homeUrl+"\">");
            out.println("\t\t\t\t"+ homeTitle);
            out.println("\t\t\t</a>");
            out.println("\t\t</div>");
            out.println("\t\t<!-- Collect the nav links, forms, and other content for toggling -->");
            out.println("\t\t<div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">");
            if (leftNavMap.size()>0) {
                out.println("\t\t\t<ul class=\"nav navbar-nav navbar-left\">");
                for (Map.Entry<String, String> entry : leftNavMap.entrySet()) {
                    out.println("\t\t\t\t<li>");
                    out.println("\t\t\t\t\t<a href='"+entry.getValue()+"'>"+entry.getKey()+"</a>");
                    out.println("\t\t\t\t</li>");
                }
                out.println("\t\t\t</ul>");
            }
            if (rightNavMap.size()>0) {
                out.println("\t\t\t<ul class=\"nav navbar-nav navbar-right\">");
                for (Map.Entry<String, String> entry : rightNavMap.entrySet()) {
                    out.println("\t\t\t\t<li>");
                    out.println("\t\t\t\t\t<a href='"+entry.getValue()+"'>"+entry.getKey()+"</a>");
                    out.println("\t\t\t\t</li>");
                }
                out.println("\t\t\t</ul>");
            }
            out.println("\t\t</div>");
            out.println("\t\t<!-- /.navbar-collapse -->");
            out.println("\t</div>");
            out.println("\t<!-- /.container -->");
            out.println("</nav>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getLeftNavMap() {
        return leftNavMap;
    }

    public void setLeftNavMap(Map<String, String> leftNavMap) {
        this.leftNavMap = leftNavMap;
    }

    public Map<String, String> getRightNavMap() {
        return rightNavMap;
    }

    public void setRightNavMap(Map<String, String> rightNavMap) {
        this.rightNavMap = rightNavMap;
    }
}
