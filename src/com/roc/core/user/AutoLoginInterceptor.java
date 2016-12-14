package com.roc.core.user;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.roc.core.Utils.WebUtil;

/**
 * User: rocwu
 * Date: 2016/11/09
 * Time: 14:06
 * Desc: 处理保持在线的interceptor
 */
public class AutoLoginInterceptor extends MethodFilterInterceptor {
    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        if (UserManager.isGuest()) {
            String authKey = WebUtil.getCookie(UserManager.AUTH_CODE_KEY);
            UserManager.login(authKey);
        }
        return actionInvocation.invoke();
    }
}
