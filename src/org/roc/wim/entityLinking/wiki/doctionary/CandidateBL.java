package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.base.BaseBL;
import org.roc.wim.entityLinking.el.entity.EntityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/12/25
 * Time: 16:11
 * Desc:
 */
@Service
public class CandidateBL extends BaseBL {

    @Autowired
    private CandidateDAO candidateDAO;

    @PostConstruct
    public void init() {
        this.dao = candidateDAO;
    }
}
