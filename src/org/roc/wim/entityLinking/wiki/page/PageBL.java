package org.roc.wim.entityLinking.wiki.page;

import com.roc.core.Utils.StringUtil;
import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: wiki page业务逻辑
 */
@Service
public class PageBL extends BaseBL {
    @Autowired
    private PageDAO pageDAO;

    @PostConstruct
    public void init() {
        this.dao = pageDAO;
    }

    public Page get(int page_namespace, String page_title) {
        if (StringUtil.isEmpty(page_title)) return null;
        page_title = page_title.replaceAll(" ", "_");
        pageDAO.where(Page.Page_Namespace+"="+page_namespace+" and " + Page.Page_Title + "='"+StringUtil.mysqlEscapeStr(page_title)+"'");
        return (Page) pageDAO.one();
    }

    public Page getPageByTitle(String title) {
        return get(0, title);
    }
}
