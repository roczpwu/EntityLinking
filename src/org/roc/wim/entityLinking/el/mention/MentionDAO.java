package org.roc.wim.entityLinking.el.mention;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:51
 * Desc: 指称数据库操作类
 */
@Repository
public class MentionDAO extends BaseDAO {
    public MentionDAO() {
        this.dtoType = Mention.class;
        this.dbName = "el";
        this.tableName = "mention";
        this.dbConnectConfig = null;
    }
}
