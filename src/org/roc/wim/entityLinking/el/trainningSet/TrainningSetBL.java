package org.roc.wim.entityLinking.el.trainningSet;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/12/21
 * Time: 14:36
 * Desc: 训练数据业务逻辑层
 */
@Service
public class TrainningSetBL extends BaseBL {
    @Autowired
    private TrainningSetDAO trainningSetDAO;

    @PostConstruct
    public void init() {
        this.dao = trainningSetDAO;
    }
}
