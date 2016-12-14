package org.roc.wim.test;

import com.roc.core.base.BaseAction;
import com.roc.core.user.UserBL;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rocwu on 16/8/4.
 */
public class TestAction extends BaseAction {

    private String message;

    @Autowired
    private UserBL userBL;

    @Override
    public String execute() throws Exception {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<");
        return SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserBL getUserBL() {
        return userBL;
    }

    public void setUserBL(UserBL userBL) {
        this.userBL = userBL;
    }
}
