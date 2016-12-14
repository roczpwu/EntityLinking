package org.roc.wim.entityLinking.el.doc;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 14:02
 * Desc: 文档业务逻辑
 */
@Service
public class DocBL extends BaseBL {
    @Autowired
    private DocDAO docDAO;

    @PostConstruct
    public void init() {
        this.dao = docDAO;
    }

    public DocDAO getDocDAO() {
        return docDAO;
    }

    public void setDocDAO(DocDAO docDAO) {
        this.docDAO = docDAO;
    }
}
