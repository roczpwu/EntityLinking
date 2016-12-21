package org.roc.wim.entityLinking.wiki.page;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 19:16
 * Desc: 实体id到wiki标题的cache
 */
@Component
public class Id2TitleCache extends BaseCache<Integer, String> {

    @Autowired
    private PageBL pageBL;

    public Id2TitleCache() {
        super(100000);
    }

    @Override
    protected String getDirectly(Integer id) {
        Page page = (Page) pageBL.get(id);
        if (page == null) return null;
        return page.getPage_title();
    }
}
