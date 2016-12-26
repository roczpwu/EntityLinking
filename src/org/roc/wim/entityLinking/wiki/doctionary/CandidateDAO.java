package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/12/25
 * Time: 16:11
 * Desc:
 */
@Repository
public class CandidateDAO extends BaseDAO {
    public CandidateDAO() {
        this.dtoType = Candidate.class;
        this.dbName = "el";
        this.tableName = "entityname";
        this.dbConnectConfig = null;
    }
}
