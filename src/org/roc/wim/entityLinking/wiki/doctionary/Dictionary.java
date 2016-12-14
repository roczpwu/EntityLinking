package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/23
 * Time: 19:37
 * Desc: google词典
 */
public class Dictionary extends BaseDTO {
    public static final String Id                   = "id";
    public static final String Name                 = "name";
    public static final String NameUpperCase        = "nameUpperCase";
    public static final String WikiTitle            = "wikiTitle";
    public static final String ProbOfNameToEntity   = "probOfNameToEntity";
    public static final String ProbOfEntityToName   = "probOfEntityToName";

    public Dictionary() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Dictionary.Id;
        this.isAutoIncrease = true;
    }

    private int id;                     // ID
    private String name;                // 实体名称
    private String nameUpperCase;       // 实体名称大写
    private String wikiTitle;           // 维基标题
    private float probOfNameToEntity;   // 从名称到实体的概率
    private float probOfEntityToName;   // 从实体到名称的概率

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUpperCase() {
        return nameUpperCase;
    }

    public void setNameUpperCase(String nameUpperCase) {
        this.nameUpperCase = nameUpperCase;
    }

    public String getWikiTitle() {
        return wikiTitle;
    }

    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }

    public float getProbOfNameToEntity() {
        return probOfNameToEntity;
    }

    public void setProbOfNameToEntity(float probOfNameToEntity) {
        this.probOfNameToEntity = probOfNameToEntity;
    }

    public float getProbOfEntityToName() {
        return probOfEntityToName;
    }

    public void setProbOfEntityToName(float probOfEntityToName) {
        this.probOfEntityToName = probOfEntityToName;
    }
}
