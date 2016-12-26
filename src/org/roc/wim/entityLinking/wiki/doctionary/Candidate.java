package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 19:27
 * Desc: 候选实体
 */
public class Candidate extends BaseDTO {

    public static final String EntityId             = "entityId";
    public static final String Name                 = "name";
    public static final String WikiTitle            = "wikiTitle";
    public static final String ProbOfNameToEntity   = "probOfNameToEntity";
    public static final String Seq                  = "seq";

    private String name;
    private String wikiTitle;
    private int entityId;
    private float probOfNameToEntity;
    private int seq;

    public Candidate() {
        this.primaryKey = new String[2];
        this.primaryKey[0] = Candidate.EntityId;
        this.primaryKey[1] = Candidate.Name;
        this.isAutoIncrease = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWikiTitle() {
        return wikiTitle;
    }

    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public float getProbOfNameToEntity() {
        return probOfNameToEntity;
    }

    public void setProbOfNameToEntity(float probOfNameToEntity) {
        this.probOfNameToEntity = probOfNameToEntity;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
