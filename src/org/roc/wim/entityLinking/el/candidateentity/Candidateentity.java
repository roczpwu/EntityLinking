package org.roc.wim.entityLinking.el.candidateentity;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:51
 * Desc: 指称的候选实体
 */
public class Candidateentity extends BaseDTO {

    public static final String Id               = "id";
    public static final String DocId            = "docId";
    public static final String SeqInDoc         = "seqInDoc";
    public static final String MentionId        = "mentionId";
    public static final String MentionName      = "mentionName";
    public static final String EntityId         = "entityId";
    public static final String WikiTitle        = "wikiTitle";
    public static final String ProbOfNameToEntity = "probOfNameToEntity";
    public static final String RankByProbOfNameToEntity = "rankByProbOfNameToEntity";
    public static final String CorrectEntityId  = "correctEntityId";

    public Candidateentity() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Candidateentity.Id;
        this.isAutoIncrease = true;
    }

    private int id;                         // ID
    private int docId;                      // 文档ID
    private int seqInDoc;                   // 文档内部指称序号
    private int mentionId;                  // 指称ID
    private String mentionName;             // 实体名称
    private int entityId;                   // 候选实体ID
    private String wikiTitle;               // 候选实体维基标题
    private float probOfNameToEntity;       // 指称名字到实体的先验概率(From Google)
    private int rankByProbOfNameToEntity;   // 指称名字到实体的概率排序
    private int correctEntityId;            // 正确的entity id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getSeqInDoc() {
        return seqInDoc;
    }

    public void setSeqInDoc(int seqInDoc) {
        this.seqInDoc = seqInDoc;
    }

    public int getMentionId() {
        return mentionId;
    }

    public void setMentionId(int mentionId) {
        this.mentionId = mentionId;
    }

    public String getMentionName() {
        return mentionName;
    }

    public void setMentionName(String mentionName) {
        this.mentionName = mentionName;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
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

    public int getRankByProbOfNameToEntity() {
        return rankByProbOfNameToEntity;
    }

    public void setRankByProbOfNameToEntity(int rankByProbOfNameToEntity) {
        this.rankByProbOfNameToEntity = rankByProbOfNameToEntity;
    }

    public int getCorrectEntityId() {
        return correctEntityId;
    }

    public void setCorrectEntityId(int correctEntityId) {
        this.correctEntityId = correctEntityId;
    }
}
