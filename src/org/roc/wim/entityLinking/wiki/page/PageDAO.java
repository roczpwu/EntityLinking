package org.roc.wim.entityLinking.wiki.page;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: wiki page数据库操作层
 */
@Repository
public class PageDAO extends BaseDAO {
    public PageDAO() {
        this.dtoType = Page.class;
        this.dbName = "wiki";
        this.tableName = "page";
        this.dbConnectConfig = "wiki";
    }
}
