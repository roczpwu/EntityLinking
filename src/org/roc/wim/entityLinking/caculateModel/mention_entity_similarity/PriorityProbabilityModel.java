package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import org.springframework.stereotype.Service;

/**
 * User: rocwu
 * Date: 2016/12/14
 * Time: 15:50
 * Desc: 计算给定指称对应候选实体的先验概率
 */
@Service
public class PriorityProbabilityModel {
    public float calcSimilarity(String mention, String title) {
        return 1.0f;
    }
}
