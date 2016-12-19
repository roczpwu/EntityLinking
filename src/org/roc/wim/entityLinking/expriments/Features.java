package org.roc.wim.entityLinking.expriments;

import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityDiceSimilarityModel;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityLinkSimilarityModel;
import org.roc.wim.entityLinking.caculateModel.mention_entity_similarity.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: rocwu
 * Date: 2016/12/19
 * Time: 19:48
 * Desc: 特征
 */
public class Features {
    public static final String PriorityProbability  = "PriorityProbability";
    public static final String ContextSimilarity    = "ContextSimilarity";
    public static final String EditDistance         = "EditDistance";
    public static final String MentionContainTitle  = "MentionContainTitle";
    public static final String TitleContainsMention = "TitleContainsMention";
    public static final String EntityDice           = "EntityDice";
    public static final String EntityLink           = "EntityLink";

    @Autowired
    private PriorityProbabilityModel priorityProbabilityModel;
    @Autowired
    private ContextSimilarityModel contextSimilarityModel;
    @Autowired
    private EditDistanceSimilarityModel editDistanceSimilarityModel;
    @Autowired
    private TitleContainsMentionModel titleContainsMentionModel;
    @Autowired
    private MentionContainsTitleModel mentionContainsTitleModel;
    @Autowired
    private EntityLinkSimilarityModel entityLinkSimilarityModel;
    @Autowired
    private EntityDiceSimilarityModel entityDiceSimilarityModel;

    private final String[] featureNames;

    public Features(String[] featureNames) {
        this.featureNames = featureNames;
    }

    public float[] generateFeatureVector(String[] mentionContext, String[] entityContext,
                                          String mention, String title, String[] contextMentions) {
        float[] values = new float[featureNames.length];
        for (int i = 0; i < featureNames.length; i++) {
            if (PriorityProbability.equals(featureNames[i])) {
                values[i] = priorityProbabilityModel.calcSimilarity(mention, title);
            } else if (ContextSimilarity.equals(featureNames[i])) {
                values[i] = contextSimilarityModel.calcSimilarity(mentionContext, entityContext);
            } else if (EditDistance.equals(featureNames[i])) {
                values[i] = editDistanceSimilarityModel.calcSimilarity(mention, title);
            } else if (MentionContainTitle.equals(featureNames[i])) {
                values[i] = mentionContainsTitleModel.calcSimilarity(mention, title);
            } else if (TitleContainsMention.equals(featureNames[i])) {
                values[i] = titleContainsMentionModel.calcSimilarity(mention, title);
            } else if (EntityDice.equals(featureNames[i])) {
                values[i] = entityDiceSimilarityModel.calcSimilarity(title, contextMentions);
            } else if (EntityLink.equals(featureNames[i])) {
                values[i] = entityLinkSimilarityModel.calcSimilarity(title, contextMentions);
            } else  {
                throw new RuntimeException("no mapped feature");
            }
        }
        return values;
    }

    public String[] getFeatureNames() {
        return featureNames;
    }
}
