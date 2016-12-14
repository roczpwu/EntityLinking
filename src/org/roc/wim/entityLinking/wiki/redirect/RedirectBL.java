package org.roc.wim.entityLinking.wiki.redirect;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 15:25
 * Desc: 重定向信息业务逻辑
 */
@Service
public class RedirectBL extends BaseBL {
    @Autowired
    private RedirectDAO redirectDAO;

    @PostConstruct
    public void init() {
        this.dao = redirectDAO;
    }
}
