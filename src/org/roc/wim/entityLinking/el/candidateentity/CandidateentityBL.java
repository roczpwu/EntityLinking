package org.roc.wim.entityLinking.el.candidateentity;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 15:15
 * Desc: 指称的候选实体业务逻辑
 */
@Service
public class CandidateentityBL extends BaseBL {
    @Autowired
    private CandidateentityDAO candidateentityDAO;

    @PostConstruct
    public void init() {
        this.dao = candidateentityDAO;
    }

    public CandidateentityDAO getCandidateentityDAO() {
        return candidateentityDAO;
    }

    public void setCandidateentityDAO(CandidateentityDAO candidateentityDAO) {
        this.candidateentityDAO = candidateentityDAO;
    }
}
