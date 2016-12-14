package com.roc.core.Utils;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * User: rocwu
 * Date: 2016/11/07
 * Time: 22:14
 * Desc: web常用函数
 */
public class WebUtil {

    /**
     * 获取cookie
     * @param name cookie名
     * @return cookie值
     */
    public static String getCookie(String name){
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals(name))
            {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 设置cookie
     * @param name cookie名字
     * @param value cookie值
     * @param expire 过期时间（秒），0表示永久
     */
    public static void setCookie(String name, String value, int expire) {
        Cookie cookie = new Cookie(name, value);
        if (expire <= 0) expire = 60 * 60 * 24 *365;
        cookie.setMaxAge(expire);
        ServletActionContext.getResponse().addCookie(cookie);
    }

}
