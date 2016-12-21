package org.roc.wim.entityLinking.wiki.page;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 19:16
 * Desc: wiki标题到实体id的cache
 */
@Component
public class Title2IdCache extends BaseCache<String, Integer> {

    @Autowired
    private PageBL pageBL;

    public Title2IdCache() {
        super(100000);
    }

    @Override
    protected Integer getDirectly(String title) {
        Page page = pageBL.getPageByTitle(title);
        if (page == null) return null;
        return page.getPage_id();
    }
}
