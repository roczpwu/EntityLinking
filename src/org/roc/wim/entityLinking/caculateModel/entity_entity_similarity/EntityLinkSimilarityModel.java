package org.roc.wim.entityLinking.caculateModel.entity_entity_similarity;

import org.roc.wim.entityLinking.wiki.doctionary.*;
import org.roc.wim.entityLinking.wiki.doctionary.Dictionary;
import org.roc.wim.entityLinking.wiki.entitytoentitylink.EntityLinkCache;
import org.roc.wim.entityLinking.wiki.page.Page;
import org.roc.wim.entityLinking.wiki.page.PageBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 14:25
 * Desc: 实体连接关系相似度计算模型
 *                         log|(g(ei)∩g(ej))| - log max(|g(ei)|,|g(ej)|)
 *      r(ei,ej) = 1.0 + -------------------------------------------------
 *                            log(Total) - log min(|g(ei)|,|g(ej)|)
 */
@Service
public class EntityLinkSimilarityModel {

    private static final int totalEntityCount = 330000000;

    @Autowired
    private EntityLinkCache entityLinkCache;
    @Autowired
    private DictionaryBL dictionaryBL;
    @Autowired
    private PageBL pageBL;

    public float calcSimilarity(int entityId1, int entityId2) {
        Set<Integer> fromEntitySet1 = entityLinkCache.get(entityId1);
        if (fromEntitySet1 == null) fromEntitySet1 = new HashSet<>();
        Set<Integer> fromEntitySet2 = entityLinkCache.get(entityId2);
        if (fromEntitySet2 == null) fromEntitySet2 = new HashSet<>();
        Set<Integer> unionSet = new HashSet<>();
        for (int entityId : fromEntitySet1) {
            if (fromEntitySet2.contains(entityId))
                unionSet.add(entityId);
        }
        int maxSize = Math.max(fromEntitySet1.size(), fromEntitySet2.size());
        int minSize = Math.min(fromEntitySet1.size(), fromEntitySet2.size());
        float result = (float) (1.0+(Math.log(unionSet.size())-Math.log(maxSize))/(Math.log(totalEntityCount)-Math.log(minSize)));
        if (result < 0) result = 0.0f;
        return result;
    }

    /**
     * 上下文环境中mention对应实体的相似度
     * @param entityId
     * @param mentions
     * @return
     */
    public float calcSimilarity(int entityId, String[] mentions) {
        if (mentions == null || mentions.length == 0) return  0.0f;
        float result = 0.0f;
        for (int i = 0; i < mentions.length; i++) {
            List<Dictionary> titles = dictionaryBL.getCandidateTitles(mentions[i]);
            if (titles.size()>0) result += calcSimilarity(entityId, titles.get(0).getId());
        }
        return result / mentions.length;
    }

    /**
     * 上下文环境中mention对应实体的相似度
     * @param title
     * @param mentions
     * @return
     */
    public float calcSimilarity(String title, String[] mentions) {
        Page page = (Page) pageBL.getByCondition(Page.Page_Title + "='"+title+"'");
        if (page == null) return 0.0f;
        return calcSimilarity(page.getPage_id(), mentions);
    }
}
