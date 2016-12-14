package com.roc.core.base;

import com.roc.core.Utils.P;
import com.roc.core.Utils.StringUtil;
import com.roc.core.database.DBConnect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/10/4
 * Time: 2:24 pm
 * Desc: 数据库表字段字典
 */
public class FieldsMap {

    private static Map<String, String[]> map;

    static  {
        map = new HashMap<>();
    }

    public static String[] getFields(BaseDAO record) throws Exception {
        String className = record.getClass().getName();
        if (map.containsKey(className))
            return cloneArr(map.get(className));
        DBConnect connect = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + record.dbName + "." + record.tableName + " where 1=2";
            connect = record.getDBConnect();
            rs = connect.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            List<String> fieldList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String fieldName = metaData.getColumnName(i+1);
                try {
                    Field dtoTypeField = BaseDAO.class.getDeclaredField("dtoType");
                    dtoTypeField.setAccessible(true);
                    Class type = (Class) dtoTypeField.get(record);
                    type.getMethod("get"+ StringUtil.upperInitial(fieldName));
                } catch (Exception e) {
                    continue;
                }
                fieldList.add(fieldName);
            }
            String[] fields = new String[fieldList.size()];
            for (int i = 0; i < fieldList.size(); i++) {
                fields[i] = fieldList.get(i);
            }
            map.put(className, fields);
            return cloneArr(fields);
        } catch (Exception e) {
            P.pfinish();
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    private static String[] cloneArr(String[] arr) {
        String[] r = new String[arr.length];
        System.arraycopy(arr, 0, r, 0, arr.length);
        return r;
    }
}
