package com.roc.core.base;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * User: rocwu
 * Date: 2016/08/4
 * Time: 10:45 pm
 * Desc: struts2 action基类
 */
public abstract class BaseAction extends ActionSupport implements ApplicationAware,
        RequestAware, SessionAware, CookiesAware, ParameterAware, ApplicationContextAware {


    private Map<String, Object> applicationMap      = null;
    private Map<String, Object> requestMap          = null;
    private Map<String, Object> sessionMap          = null;
    private Map<String, String> cookieMap           = null; //只读
    private Map<String, String[]> parameterMap      = null;
    private ApplicationContext applicationContext   = null;

    @Override
    public void setApplication(Map<String, Object> map) {
        this.applicationMap = map;
    }

    @Override
    public void setCookiesMap(Map<String, String> map) {
        this.cookieMap = map;
    }

    @Override
    public void setParameters(Map<String, String[]> map) {
        this.parameterMap = map;
    }

    @Override
    public void setRequest(Map<String, Object> map) {
        this.requestMap = map;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.sessionMap = map;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, Object> getApplicationMap() {
        return applicationMap;
    }

    public void setApplicationMap(Map<String, Object> applicationMap) {
        this.applicationMap = applicationMap;
    }

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
