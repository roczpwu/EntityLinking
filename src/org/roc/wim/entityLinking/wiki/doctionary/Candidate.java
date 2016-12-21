package org.roc.wim.entityLinking.wiki.doctionary;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 19:27
 * Desc: 候选实体
 */
public class Candidate {

    private String name;
    private String wikiTitle;
    private int entityId;
    private float probOfNameToEntity;

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
}
