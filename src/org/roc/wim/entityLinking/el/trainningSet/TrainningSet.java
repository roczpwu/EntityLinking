package org.roc.wim.entityLinking.el.trainningSet;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/12/21
 * Time: 14:36
 * Desc: 训练数据
 */
public class TrainningSet extends BaseDTO {
    public static final String FId                  = "fid";
    public static final String MentionId            = "mentionId";
    public static final String Name                 = "name";
    public static final String EntityId             = "entityId";
    public static final String Title                = "title";
    public static final String PriorityProbability  = "priorityProbability";
    public static final String ContextSimilarity    = "contextSimilarity";
    public static final String EditDistance         = "editDistance";
    public static final String MentionContainTitle  = "mentionContainTitle";
    public static final String TitleContainsMention = "titleContainsMention";
    public static final String EntityDice           = "entityDice";
    public static final String EntityLink           = "entityLink";
    public static final String IsCorrect            = "isCorrect";

    public TrainningSet() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = TrainningSet.FId;
        this.isAutoIncrease = true;
    }

    private int fid;
    private int mentionId;
    private String name;
    private int entityId;
    private String title;
    private float priorityProbability;
    private float contextSimilarity;
    private float editDistance;
    private float mentionContainTitle;
    private float titleContainsMention;
    private float entityDice;
    private float entityLink;
    private int isCorrect;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getMentionId() {
        return mentionId;
    }

    public void setMentionId(int mentionId) {
        this.mentionId = mentionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getPriorityProbability() {
        return priorityProbability;
    }

    public void setPriorityProbability(float priorityProbability) {
        this.priorityProbability = priorityProbability;
    }

    public float getContextSimilarity() {
        return contextSimilarity;
    }

    public void setContextSimilarity(float contextSimilarity) {
        this.contextSimilarity = contextSimilarity;
    }

    public float getEditDistance() {
        return editDistance;
    }

    public void setEditDistance(float editDistance) {
        this.editDistance = editDistance;
    }

    public float getMentionContainTitle() {
        return mentionContainTitle;
    }

    public void setMentionContainTitle(float mentionContainTitle) {
        this.mentionContainTitle = mentionContainTitle;
    }

    public float getTitleContainsMention() {
        return titleContainsMention;
    }

    public void setTitleContainsMention(float titleContainsMention) {
        this.titleContainsMention = titleContainsMention;
    }

    public float getEntityDice() {
        return entityDice;
    }

    public void setEntityDice(float entityDice) {
        this.entityDice = entityDice;
    }

    public float getEntityLink() {
        return entityLink;
    }

    public void setEntityLink(float entityLink) {
        this.entityLink = entityLink;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }
}
