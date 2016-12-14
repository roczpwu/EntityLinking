package org.roc.wim.entityLinking.el.entity;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 16:16
 * Desc: 实体数据库操作层
 */
@Repository
public class EntityDAO extends BaseDAO {
    public EntityDAO() {
        this.dtoType = Entity.class;
        this.dbName = "el";
        this.tableName = "entity";
        this.dbConnectConfig = null;
    }
}
