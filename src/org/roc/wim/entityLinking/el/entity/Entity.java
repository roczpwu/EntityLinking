package org.roc.wim.entityLinking.el.entity;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 16:16
 * Desc: 实体
 */
public class Entity extends BaseDTO {

    public static final String Id = "id";
    public static final String WikiPageId = "wikiPageId";
    public static final String WikiTitle = "wikiTitle";
    public static final String Abst = "abst";
    public static final String OutlinkCount = "outlinkCount";
    public static final String InlinkCount = "inlinkCount";

    public Entity() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Entity.Id;
        this.isAutoIncrease = true;
    }

    private int id;             // ID
    private int wikiPageId;     // 维基百科页面ID
    private String wikiTitle;   // 维基百科标题
    private String abst;        // 摘要
    private int outlinkCount;   // 出度
    private int inlinkCount;    // 入度

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWikiPageId() {
        return wikiPageId;
    }

    public void setWikiPageId(int wikiPageId) {
        this.wikiPageId = wikiPageId;
    }

    public String getWikiTitle() {
        return wikiTitle;
    }

    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }

    public int getOutlinkCount() {
        return outlinkCount;
    }

    public void setOutlinkCount(int outlinkCount) {
        this.outlinkCount = outlinkCount;
    }

    public int getInlinkCount() {
        return inlinkCount;
    }

    public void setInlinkCount(int inlinkCount) {
        this.inlinkCount = inlinkCount;
    }
}
