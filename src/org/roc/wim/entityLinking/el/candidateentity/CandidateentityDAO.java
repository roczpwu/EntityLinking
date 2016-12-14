package org.roc.wim.entityLinking.el.candidateentity;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 15:15
 * Desc: 指称的候选实体数据库操作层
 */
@Repository
public class CandidateentityDAO extends BaseDAO {
    public CandidateentityDAO() {
        this.dtoType = Candidateentity.class;
        this.dbName = "el";
        this.tableName = "candidateentity";
        this.dbConnectConfig = null;
    }
}
