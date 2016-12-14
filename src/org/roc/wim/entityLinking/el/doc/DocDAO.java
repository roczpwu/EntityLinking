package org.roc.wim.entityLinking.el.doc;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:58
 * Desc: 文档数据库操作
 */
@Repository
public class DocDAO extends BaseDAO {
    public DocDAO() {
        this.dtoType = Doc.class;
        this.dbName = "el";
        this.tableName = "doc";
        this.dbConnectConfig = null;
    }
}
