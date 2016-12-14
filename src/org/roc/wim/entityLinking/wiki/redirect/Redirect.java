package org.roc.wim.entityLinking.wiki.redirect;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: 重定向信息
 */
public class Redirect extends BaseDTO {

    public static String Rd_From        = "rd_from";
    public static String Rd_Namespace   = "rd_namespace";   // 0表示主条目（基本关注0即可）
    public static String Rd_Title       = "rd_title";
    public static String Rd_Interwiki   = "rd_interwiki";
    public static String Rd_Fragment    = "rd_fragment";
    public static String Rd_Page_Id     = "rd_page_id";

    public Redirect() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Rd_From;
        this.isAutoIncrease = false;
    }

    private int rd_from;            // 重定向源ID
    private int rd_namespace;       //
    private String rd_title;        // 重定向后的维基标题
    private String rd_interwiki;    //
    private String rd_fragment;     //
    private int rd_page_id;         // 重定向目标id

    public int getRd_from() {
        return rd_from;
    }

    public void setRd_from(int rd_from) {
        this.rd_from = rd_from;
    }

    public int getRd_namespace() {
        return rd_namespace;
    }

    public void setRd_namespace(int rd_namespace) {
        this.rd_namespace = rd_namespace;
    }

    public String getRd_title() {
        return rd_title;
    }

    public void setRd_title(String rd_title) {
        this.rd_title = rd_title;
    }

    public String getRd_interwiki() {
        return rd_interwiki;
    }

    public void setRd_interwiki(String rd_interwiki) {
        this.rd_interwiki = rd_interwiki;
    }

    public String getRd_fragment() {
        return rd_fragment;
    }

    public void setRd_fragment(String rd_fragment) {
        this.rd_fragment = rd_fragment;
    }

    public int getRd_page_id() {
        return rd_page_id;
    }

    public void setRd_page_id(int rd_page_id) {
        this.rd_page_id = rd_page_id;
    }
}
