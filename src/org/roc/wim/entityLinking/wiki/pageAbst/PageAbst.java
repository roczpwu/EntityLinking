package org.roc.wim.entityLinking.wiki.pageAbst;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 16:18
 * Desc: 实体摘要
 */
public class PageAbst extends BaseDTO {
    public static final String Id       = "id";
    public static final String EntityId = "entityId";
    public static final String Title    = "title";
    public static final String Abst     = "abst";

    public PageAbst() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = PageAbst.Id;
        this.isAutoIncrease = false;
    }

    private int id;         // id
    private int entityId;   // 实体id（和id相同）
    private String title;   // 实体标题
    private String abst;    // 实体摘要

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }
}
