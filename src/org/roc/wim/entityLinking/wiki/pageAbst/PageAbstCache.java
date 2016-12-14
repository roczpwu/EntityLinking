package org.roc.wim.entityLinking.wiki.pageAbst;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 19:55
 * Desc: 实体摘要缓存
 */
@Component
public class PageAbstCache extends BaseCache<Integer, String> {

    @Autowired
    private PageAbstBL pageAbstBL;

    public PageAbstCache() {
        super(10000);
    }

    @Override
    protected String getDirectly(Integer key) {
        PageAbst pageAbst = (PageAbst) pageAbstBL.get(key);
        if (pageAbst == null) return null;
        return pageAbst.getAbst();
    }
}
