package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 14:25
 * Desc: 指称和实体之间上下文相似度计算模型
 */
@Service
public class ContextSimilarityModel {

    /**
     * 计算mention与entity上下文相似度
     * @param mentionContext mention上下文单词数组
     * @param entityContext entity词条摘要数组
     * @return 相似度
     */
    public float calcSimilarity(String[] mentionContext, String[] entityContext) {
        if (mentionContext == null || entityContext == null ||
                mentionContext.length == 0 || entityContext.length == 0)
            return 0.0f;
        int cooccurence_number = 0;
        Set<String> entityContextSet = new HashSet<>();
        Collections.addAll(entityContextSet, entityContext);
        for (String s : mentionContext) {
            if (entityContextSet.contains(s))
                cooccurence_number++;
        }
        int doc_length = mentionContext.length;
        return (float) (cooccurence_number*1.0/doc_length);
    }

    public float calcSimilarity(List<String> mentionContext, List<String> entityContext) {
        if (mentionContext == null || entityContext == null)
            return 0.0f;
        String[] arr1 = new String[mentionContext.size()];
        String[] arr2 = new String[entityContext.size()];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = mentionContext.get(i);
        }
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = entityContext.get(i);
        }
        return this.calcSimilarity(arr1, arr2);
    }
}
