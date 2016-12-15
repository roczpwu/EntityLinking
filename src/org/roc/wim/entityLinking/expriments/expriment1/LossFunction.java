package org.roc.wim.entityLinking.expriments.expriment1;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityDiceSimilarityCache;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityLinkSimilarityCache;
import org.roc.wim.entityLinking.caculateModel.mention_entity_similarity.*;
import org.roc.wim.entityLinking.el.entity.EntityBL;
import org.roc.wim.entityLinking.utils.StanfordUtil;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: rocwu
 * Date: 2016/12/13
 * Time: 14:25
 * Desc: 损失函数模型
 *       show in "loss function.pdf"
 */
@Component
public class LossFunction {
    private static float alpha1 = 0.2f;
    private static float alpha2 = 0.2f;
    private static float alpha3 = 0.2f;
    private static float alpha4 = 0.2f;
    private static float learnSpeed = 0.2f; // 学习率

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

    private float[] local_weights;
    private float[] global_weights;

    public LossFunction() {
        local_weights = new float[5];
        global_weights = new float[2];
        Random r = new Random(0);
        for (int i = 0; i < 5; i++)
            local_weights[i] = r.nextFloat();
        for (int i = 0; i < 2; i++)
            global_weights[i] = r.nextFloat();
    }

    /**
     * 迭代调整参数
     * @param mentions
     * @param mentionDoc
     * @param candidateIds
     * @param index
     */
    public void iteration(String[] mentions, String mentionDoc, int[] candidateIds, int index) {
        int n = mentions.length;
        // 总的损失结构
        DerivativeStructure lossDerivativeStructure;
        // local feature损失
        DerivativeStructure localDerivativeStructure;
        // 计算 local feature
        DerivativeStructure localDerivativeStructure_v = null;  // local 损失项
        DerivativeStructure localDerivativeStructure_r = null;  // local 正则化项
        for (int i = 0; i < n; i++) {
            String entityTitle = entityBL.getWikiTitleById(candidateIds[i]);
            String entityAbst = pageAbstBL.getAbstById(candidateIds[i]);
            float[] features = new float[5];
            features[0] = priorityProbabilityModel.calcSimilarity(mentions[i], entityTitle);
            features[1] = contextSimilarityModel.calcSimilarity(StanfordUtil.Tokenize(mentionDoc), StanfordUtil.Tokenize(entityAbst));
            features[2] = editDistanceSimilarityModel.calcSimilarity(mentions[i], entityTitle);
            features[3] = titleContainsMentionModel.calcSimilarity(mentions[i], entityTitle);
            features[4] = mentionContainsTitleModel.calcSimilarity(mentions[i], entityTitle);
            DerivativeStructure tmp_v = null;
            for (int j = 0; j < 5; j++) {
                DerivativeStructure tmp_iter_v = new DerivativeStructure(7, 1, j, local_weights[j]).multiply(features[j]);
                DerivativeStructure tmp_iter_r = new DerivativeStructure(7, 1, j, local_weights[j]).pow(2);
                if (tmp_v == null) tmp_v = tmp_iter_v;
                else tmp_v = new DerivativeStructure(1.0, tmp_v, 1.0, tmp_iter_v);
                if (localDerivativeStructure_r == null) localDerivativeStructure_r = tmp_iter_r;
                else localDerivativeStructure_r = new DerivativeStructure(1.0, localDerivativeStructure_r, 1.0, tmp_iter_r);
            }
            localDerivativeStructure_v = tmp_v.add(1.0).pow(-1);
            if (localDerivativeStructure_v == null) localDerivativeStructure_v = tmp_v;
            else localDerivativeStructure_v = new DerivativeStructure(1.0, localDerivativeStructure_v, 1.0, tmp_v);
        }
        localDerivativeStructure = new DerivativeStructure(alpha1, localDerivativeStructure_v, alpha2, localDerivativeStructure_r);
        // global feature损失
        DerivativeStructure globalDerivativeStructure;
        // 计算 global feature
        DerivativeStructure globalDerivativeStructure_v = null;  // global 损失项
        DerivativeStructure globalDerivativeStructure_r = null;  // global 正则化项
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                float[] features = new float[2];
                features[0] = entityLinkSimilarityCache.get(candidateIds[i], candidateIds[j]);
                features[1] = entityDiceSimilarityCache.get(candidateIds[i], candidateIds[j]);
                DerivativeStructure tmp_v = null;
                for (int k = 0; k < 2; k++) {
                    DerivativeStructure tmp_iter_v = new DerivativeStructure(7, 1, 5 + j, global_weights[j]).multiply(features[j]);
                    DerivativeStructure tmp_iter_r = new DerivativeStructure(7, 1, 5 + j, global_weights[j]).pow(2);
                    if (tmp_v == null) tmp_v = tmp_iter_v;
                    else tmp_v = new DerivativeStructure(1.0, tmp_v, 1.0, tmp_iter_v);
                    if (globalDerivativeStructure_r == null) globalDerivativeStructure_r = tmp_iter_r;
                    else globalDerivativeStructure_r = new DerivativeStructure(1.0, globalDerivativeStructure_r, 1.0, tmp_iter_r);
                }
                globalDerivativeStructure_v = tmp_v.add(1.0).pow(-1);
                if (globalDerivativeStructure_v == null) globalDerivativeStructure_v = tmp_v;
                else globalDerivativeStructure_v = new DerivativeStructure(1.0, globalDerivativeStructure_v, 1.0, tmp_v);
            }
        }
        globalDerivativeStructure = new DerivativeStructure(alpha3, globalDerivativeStructure_v, alpha4, globalDerivativeStructure_r);
        // 综合local loss和global loss
        lossDerivativeStructure = new DerivativeStructure(1.0, localDerivativeStructure, 1.0, globalDerivativeStructure);
        int[] orders = new int[7];
        orders[index] = 1;
        double derivative = lossDerivativeStructure.getPartialDerivative(orders);
        if (index < 5) local_weights[index] -= derivative * learnSpeed;
        else global_weights[index] -= derivative * learnSpeed;
    }
}
