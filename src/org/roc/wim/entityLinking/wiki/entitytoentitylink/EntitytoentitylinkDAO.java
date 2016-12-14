package org.roc.wim.entityLinking.wiki.entitytoentitylink;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 16:23
 * Desc: 实体之间链接关系数据库操作层
 */
@Repository
public class EntitytoentitylinkDAO extends BaseDAO {
    public EntitytoentitylinkDAO() {
        this.dtoType = Entitytoentitylink.class;
        this.dbName = "wiki";
        this.tableName = "entitytoentitylink";
        this.dbConnectConfig = "wiki";
    }
}
