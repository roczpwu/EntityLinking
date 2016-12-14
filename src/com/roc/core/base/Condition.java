package com.roc.core.base;

import com.roc.core.Utils.StringUtil;

import java.util.Objects;

/**
 * User: rocwu
 * Date: 2016/07/28
 * Time: 0:31 am
 * Desc: 持久层查询条件
 */
public class Condition {

    public static String where(Object condition) {
        if (condition==null)
            return "";
        if (condition instanceof String)
            return (String)condition;
        if (!(condition instanceof Object[]))
            throw new RuntimeException("解析错误");
        return parseWhereCondition((Object[]) condition);
    }

    private static String parseWhereCondition(Object[] condition) {
        String whereSql = "";
        if (condition==null) return whereSql;
        int count = condition.length;
        if (count == 0) throw new RuntimeException("解析错误");
        if (condition[0] instanceof String) {
            // 最小条件处理
            if (count < 2) throw new RuntimeException("解析错误");
            String operator = count == 3 ? StringUtil.trim((String) condition[2]).toUpperCase() : "=";
            String field = StringUtil.trim((String) condition[0]);
            Object value = condition[1];
            if (Operations.IN.equals(operator)) {
                // 处理key in的情况
                Object[]values = (Object[]) value;
                if (values.length == 0) throw new RuntimeException("解析错误");
                StringBuilder sb = new StringBuilder();
                for (Object v : values) {
                    if (sb.length()>0) sb.append(", ");
                    sb.append("'"+StringUtil.mysqlEscapeStr(v.toString())+"'");
                }
                whereSql = field + " IN (" + sb.toString() + ")";
            } else if (value == null) {
                // 处理value为null的情况
                whereSql = field+" "+operator+" null";
            } else if (Operations.BETWEEN.equals(operator)) {
                // 处理between的情况
                Object[]values = (Object[]) value;
                whereSql = field+" "+operator+" ('"+StringUtil.mysqlEscapeStr((String) values[0])+
                        "' AND '"+StringUtil.mysqlEscapeStr((String) values[1])+"')";
            }
            else {
                whereSql = field+" "+operator+" '"+StringUtil.mysqlEscapeStr((String) value)+"'";
            }
            return whereSql;
        } else {
            // 包含子条件语句
            whereSql = "";
            String relation = "AND";
            Object lastObj = condition[count-1];
            if (lastObj instanceof String && ("AND".equals(StringUtil.trim((String) lastObj).toUpperCase())
                    ||("OR".equals(StringUtil.trim((String) lastObj).toUpperCase())))) {
                relation = StringUtil.trim((String) lastObj).toUpperCase();
                count--;
            }
            for (int i=0;i<count;i++) {
                String subSql = parseWhereCondition((Object[]) condition[i]);
                if (!StringUtil.isEmpty(subSql)) {
                    if (StringUtil.isEmpty(whereSql))
                        whereSql = subSql;
                    else
                        whereSql = whereSql+" "+relation+" "+subSql;
                }
            }
            if (count>1) whereSql = "("+whereSql+")";
        }
        return whereSql;
    }

}