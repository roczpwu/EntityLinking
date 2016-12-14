package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/23
 * Time: 19:37
 * Desc: google词典数据库操作层
 */
@Repository
public class DictionaryDAO extends BaseDAO {
    public DictionaryDAO() {
        this.dtoType = Dictionary.class;
        this.dbName = "wiki";
        this.tableName = "dictionary";
        this.dbConnectConfig = "wiki";
    }
}
