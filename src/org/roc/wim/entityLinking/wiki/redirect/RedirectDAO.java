package org.roc.wim.entityLinking.wiki.redirect;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: 重定向信息数据库操作层
 */
@Repository
public class RedirectDAO extends BaseDAO {
    public RedirectDAO() {
        this.dtoType = Redirect.class;
        this.dbName = "wiki";
        this.tableName = "redirect";
        this.dbConnectConfig = "wiki";
    }
}
