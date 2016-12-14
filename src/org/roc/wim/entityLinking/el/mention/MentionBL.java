package org.roc.wim.entityLinking.el.mention;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 13:51
 * Desc: 指称业务逻辑
 */
@Service
public class MentionBL extends BaseBL {
    @Autowired
    private MentionDAO mentionDAO;

    @PostConstruct
    public void init() {
        this.dao = mentionDAO;
    }

    public MentionDAO getMentionDAO() {
        return mentionDAO;
    }

    public void setMentionDAO(MentionDAO mentionDAO) {
        this.mentionDAO = mentionDAO;
    }
}
