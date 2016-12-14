package com.roc.core.user;

import com.roc.core.base.BaseBL;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * User: rocwu
 * Date: 2016/10/24
 * Time: 11:15 am
 * Desc: 用户对象业务逻辑层
 */
public class UserBL extends BaseBL {

    @Autowired
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        this.dao = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
