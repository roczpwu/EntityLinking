package org.roc.wim.entityLinking.el.entity;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 16:16
 * Desc: 实体业务逻辑
 */
@Service
public class EntityBL extends BaseBL {

    @Autowired
    private EntityDAO entityDAO;

    @PostConstruct
    public void init() {
        this.dao = entityDAO;
    }

    public EntityDAO getEntityDAO() {
        return entityDAO;
    }

    public void setEntityDAO(EntityDAO entityDAO) {
        this.entityDAO = entityDAO;
    }
}
