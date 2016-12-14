package org.roc.wim.entityLinking.el.doc;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:51
 * Desc: 文档
 */
public class Doc extends BaseDTO {

    public static final String Id       = "id";
    public static final String Title    = "title";
    public static final String Tokens   = "tokens";
    public static final String Url      = "url";
    public static final String ToeTag   = "toeTag";
    public static final String PubDate  = "pubDate";
    public static final String Folder   = "folder";

    private int id;
    private String title;
    private String content;
    private String tokens;
    private String url;
    private String toeTag;
    private String pubDate;
    private String folder;

    public Doc() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Doc.Id;
        this.isAutoIncrease = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToeTag() {
        return toeTag;
    }

    public void setToeTag(String toeTag) {
        this.toeTag = toeTag;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
