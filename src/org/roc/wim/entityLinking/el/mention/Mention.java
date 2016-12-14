package org.roc.wim.entityLinking.el.mention;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:51
 * Desc: 指称
 */
public class Mention extends BaseDTO {

    public static final String Id = "id";
    public static final String DocId = "docId";
    public static final String SeqInDoc = "seqInDoc";
    public static final String SentenceSeq = "sentenceSeq";
    public static final String StartIdx = "startIdx";
    public static final String EndIdx = "endIdx";
    public static final String StartCharIdx = "startCharIdx";
    public static final String EndCharIdx = "endCharIdx";
    public static final String Name = "name";
    public static final String EntityId = "entityId";
    public static final String WikiTitle = "wikiTitle";
    public static final String Tf = "tf";
    public static final String Idf = "idf";
    public static final String Tfidf = "tfidf";
    public static final String ToeTag = "toeTag";

    public Mention() {
        this.primaryKey = new String[1];
        this.primaryKey[0] = Mention.Id;
        this.isAutoIncrease = true;
    }

    private int id;
    private int docId;
    private int seqInDoc;
    private int sentenceSeq;
    private int startIdx;
    private int endIdx;
    private int startCharIdx;
    private int endCharIdx;
    private String name;
    private int entityId;
    private String wikiTitle;
    private float tf;
    private float idf;
    private float tfidf;
    private String toeTag;

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

    public int getSentenceSeq() {
        return sentenceSeq;
    }

    public void setSentenceSeq(int sentenceSeq) {
        this.sentenceSeq = sentenceSeq;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }

    public int getStartCharIdx() {
        return startCharIdx;
    }

    public void setStartCharIdx(int startCharIdx) {
        this.startCharIdx = startCharIdx;
    }

    public int getEndCharIdx() {
        return endCharIdx;
    }

    public void setEndCharIdx(int endCharIdx) {
        this.endCharIdx = endCharIdx;
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

    public String getWikiTitle() {
        return wikiTitle;
    }

    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }

    public float getTf() {
        return tf;
    }

    public void setTf(float tf) {
        this.tf = tf;
    }

    public float getIdf() {
        return idf;
    }

    public void setIdf(float idf) {
        this.idf = idf;
    }

    public float getTfidf() {
        return tfidf;
    }

    public void setTfidf(float tfidf) {
        this.tfidf = tfidf;
    }

    public String getToeTag() {
        return toeTag;
    }

    public void setToeTag(String toeTag) {
        this.toeTag = toeTag;
    }
}
