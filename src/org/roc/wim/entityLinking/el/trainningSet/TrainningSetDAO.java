package org.roc.wim.entityLinking.el.trainningSet;

import com.roc.core.base.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * User: rocwu
 * Date: 2016/12/21
 * Time: 14:36
 * Desc: 训练数据数据库操作层
 */
@Repository
public class TrainningSetDAO extends BaseDAO {
    public TrainningSetDAO() {
        this.dtoType = TrainningSet.class;
        this.dbName = "el";
        this.tableName = "trainningSet";
        this.dbConnectConfig = null;
    }
}
