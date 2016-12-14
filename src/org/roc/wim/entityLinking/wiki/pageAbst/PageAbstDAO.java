package org.roc.wim.entityLinking.wiki.pageAbst;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 19:55
 * Desc: 实体摘要数据库操作层
 */
@Repository
public class PageAbstDAO extends BaseDAO {
    public PageAbstDAO() {
        this.dtoType = PageAbst.class;
        this.dbName = "wiki";
        this.tableName = "pageabst";
        this.dbConnectConfig = "wiki";
    }
}
