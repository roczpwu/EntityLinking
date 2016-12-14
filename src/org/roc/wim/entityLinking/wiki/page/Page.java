package org.roc.wim.entityLinking.wiki.page;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: wiki page
 */
public class Page extends BaseDTO {

    public static final String Page_Id          = "page_id";
    public static final String Page_Namespace   = "page_namespace";
    public static final String Page_Title       = "page_title";

    public Page() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Page_Id;
        this.isAutoIncrease = true;
    }

    private int page_id;
    private int page_namespace;
    private String page_title;

    public int getPage_id() {
        return page_id;
    }

    public void setPage_id(int page_id) {
        this.page_id = page_id;
    }

    public int getPage_namespace() {
        return page_namespace;
    }

    public void setPage_namespace(int page_namespace) {
        this.page_namespace = page_namespace;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }
}
