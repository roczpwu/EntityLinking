package com.roc.core.base;

import com.roc.core.Utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: rocwu
 * Date: 2016/08/4
 * Time: 10:45 pm
 * Desc: 数据库对象层
 */
public class BaseDTO {

    protected String[] primaryKey = null;
    protected boolean isRelatedWithDB = false;
    protected boolean isAutoIncrease = false;
    protected String keyCondition = null;

    public boolean getIsRelatedWithDB() {
        return isRelatedWithDB;
    }

    public String getKeyCondition() {
        if (!isRelatedWithDB)
            throw new RuntimeException("DTO目前处于游离态，无法获取唯一主键条件");
        return keyCondition;
    }

    protected void updateKeyCondition() {
        isRelatedWithDB = true;
        StringBuilder sb = new StringBuilder();
        for (String key : primaryKey) {
            //key = key.toLowerCase();
            sb.append(key).append("=");
            try {
                Method getMethod = this.getClass().getMethod("get"+ StringUtil.upperInitial(key));
                Object obj = getMethod.invoke(this);
                sb.append("'").append(StringUtil.mysqlEscapeStr(obj.toString())).append("'");
                sb.append(" and ");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.keyCondition = sb.substring(0, sb.length()-5);
    }

}
