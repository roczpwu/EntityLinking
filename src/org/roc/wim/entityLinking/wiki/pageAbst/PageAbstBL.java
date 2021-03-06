package org.roc.wim.entityLinking.wiki.pageAbst;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 19:55
 * Desc: 实体摘要业务逻辑
 */
@Service
public class PageAbstBL extends BaseBL {
    @Autowired
    private PageAbstDAO pageAbstDAO;

    @PostConstruct
    public void init() {
        this.dao = pageAbstDAO;
    }

    public String getAbstById(int entityId) {
        PageAbst pageAbst = (PageAbst) this.pageAbstDAO.fields(PageAbst.Abst).where(PageAbst.EntityId+"='"+entityId+"'").one();
        if (pageAbst == null) return null;
        return pageAbst.getAbst();
    }

    public String getAbstByTitle(String wikiTitle) {
        PageAbst pageAbst = (PageAbst) this.pageAbstDAO.fields(PageAbst.Abst).where(PageAbst.Title+"='"+wikiTitle+"'").one();
        if (pageAbst == null) return null;
        return pageAbst.getAbst();
    }
}
