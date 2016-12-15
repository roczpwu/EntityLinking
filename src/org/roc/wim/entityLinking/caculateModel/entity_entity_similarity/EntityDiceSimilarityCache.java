package org.roc.wim.entityLinking.caculateModel.entity_entity_similarity;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/12/12
 * Time: 16:25
 * Desc: 实体摘要之间Dice相似度计算结果cache
 *                   2 * |abst_token(ei)∩abst_token(ej)|
 *      r(ei,ej) = ----------------------------------------
 *                    |abst_token(ei)|+|abst_token(ej)|
 */
@Component
public class EntityDiceSimilarityCache extends BaseCache<String, Float> {
    @Autowired
    private EntityDiceSimilarityModel entityDiceSimilarityModel;

    public EntityDiceSimilarityCache() {
        super(10000);
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
        return entityDiceSimilarityModel.calcSimilarity(entityId1, entityId2);
    }
}
