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
}
