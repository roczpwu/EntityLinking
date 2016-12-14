package org.roc.wim.entityLinking.wiki.entitytoentitylink;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 16:18
 * Desc: 缓存以下内容
 *      给定一个实体，获取存在链接到该实体的实体集合
 */
@Component
public class EntityLinkCache extends BaseCache<Integer, Set<Integer>> {

    @Autowired
    private EntitytoentitylinkBL entitytoentitylinkBL;

    public EntityLinkCache() {
        super(10000);
    }

    @Override
    protected Set<Integer> getDirectly(Integer toEntityId) {
        Set<Integer> result = entitytoentitylinkBL.getFromEntityListByToEntity(toEntityId);
        if (result.isEmpty())
            result = null;
        return result;
    }
}
