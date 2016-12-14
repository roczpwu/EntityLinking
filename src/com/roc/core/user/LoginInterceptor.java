package com.roc.core.user;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.roc.core.BLMessage;
import com.roc.core.Utils.StringUtil;
import com.roc.core.Utils.WebUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * User: rocwu
 * Date: 2016/10/25
 * Time: 15:07
 * Desc: 检查登录态的拦截器
 */
public class LoginInterceptor extends MethodFilterInterceptor {

    @Autowired
    private UserBL userBL;

    @Override
    protected String doIntercept(ActionInvocation invoke) throws Exception {
        if (UserManager.isGuest()) {
            String authKey = WebUtil.getCookie(UserManager.AUTH_CODE_KEY);
            BLMessage blMessage = UserManager.login(authKey);
            if (!blMessage.isSuccess())
                return "login";
        }
        return invoke.invoke();
    }

    public UserBL getUserBL() {
        return userBL;
    }

    public void setUserBL(UserBL userBL) {
        this.userBL = userBL;
    }
}
