package org.roc.wim.entityLinking.expriments.expriment1;

import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityDiceSimilarityCache;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityLinkSimilarityCache;
import org.roc.wim.entityLinking.caculateModel.mention_entity_similarity.*;
import org.roc.wim.entityLinking.el.entity.EntityBL;
import org.roc.wim.entityLinking.utils.StanfordUtil;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/12/14
 * Time: 14:29
 * Desc: 候选实体概率计算模型
 *       show in "prob function.pdf"
 */
@Component
public class ProbabilityFunction {
    private static float lambda = 0.5f;

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
    private EntityLinkSimilarityCache entityLinkSimilarityCache;
    @Autowired
    private EntityDiceSimilarityCache entityDiceSimilarityCache;
    @Autowired
    private EntityBL entityBL;
    @Autowired
    private PageAbstBL pageAbstBL;

    private float[] local_weights = null;      // 局部权重
    private float[] global_weights = null;     // 全局权重

    public void init(float[] local_weights, float[] global_weights) {
        this.local_weights = local_weights;
        this.global_weights = global_weights;
    }

    /**
     * 计算候选实体概率
     * @param mentions
     * @param mentionDoc
     * @param candidateIds
     * @return
     */
    public float calcProbability(String[] mentions, String mentionDoc, int[] candidateIds) {
        int n = mentions.length;
        float localSimilarity = 0.0f;
        float golobalSimilarity = 0.0f;
        for (int i = 0; i < n; i++) {
            String entityTitle = entityBL.getWikiTitleById(candidateIds[i]);
            String entityAbst = pageAbstBL.getAbstById(candidateIds[i]);
            float[] features = new float[5];
            features[0] = priorityProbabilityModel.calcSimilarity(mentions[i], entityTitle);
            features[1] = contextSimilarityModel.calcSimilarity(StanfordUtil.Tokenize(mentionDoc), StanfordUtil.Tokenize(entityAbst));
            features[2] = editDistanceSimilarityModel.calcSimilarity(mentions[i], entityTitle);
            features[3] = titleContainsMentionModel.calcSimilarity(mentions[i], entityTitle);
            features[4] = mentionContainsTitleModel.calcSimilarity(mentions[i], entityTitle);
            for (int m = 0; m < 5; m++) {
                localSimilarity += features[m] * local_weights[m];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                float[] features = new float[2];
                features[0] = entityLinkSimilarityCache.get(candidateIds[i], candidateIds[j]);
                features[1] = entityDiceSimilarityCache.get(candidateIds[i], candidateIds[j]);
                for (int k = 0; k < 2; k++) {
                    golobalSimilarity += features[k] * global_weights[k];
                }
            }
        }
        return lambda * localSimilarity + (1.0f - lambda) * golobalSimilarity;
    }
}
