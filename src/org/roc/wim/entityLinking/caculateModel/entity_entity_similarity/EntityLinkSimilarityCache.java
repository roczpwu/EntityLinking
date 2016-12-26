package org.roc.wim.entityLinking.caculateModel.entity_entity_similarity;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 14:25
 * Desc: 实体相似度计算结果cache
 */
@Component
public class EntityLinkSimilarityCache extends BaseCache<String, Float> {

    @Autowired
    private EntityLinkSimilarityModel entityLinkSimilarityModel;

    public EntityLinkSimilarityCache() {
        super(1000000);
    }

    public float get(int entityId1, int entityId2) {
        if (entityId1<=entityId2)
            return get(entityId1+","+entityId2);
        else
            return get(entityId2+","+entityId1);
    }

    @Override
    protected Float getDirectly(String key) {
        String []entityIds = key.split(",");
        int entityId1 = Integer.parseInt(entityIds[0]);
        int entityId2 = Integer.parseInt(entityIds[1]);
        return entityLinkSimilarityModel.calcSimilarity(entityId1, entityId2);
    }
}
