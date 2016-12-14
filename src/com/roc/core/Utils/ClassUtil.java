package com.roc.core.Utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Lu Tingming
 * Datetime 2010-12-18 10:09:40
 * Desc: 类工具
 */
public class ClassUtil {
    /**
     * 得到从名字到字段的映射表
     * @param clazz
     * @return
     */
    public static HashMap<String, Field> getNameToFieldMap(Class clazz) {
        HashMap<String, Field> map = new HashMap<String, Field>();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            map.put(field.getName().toLowerCase(), field);
        }

        return map;
    }

    /**
     * 得到对象的类的字段名
     * @param obj
     * @return
     */
    public static String[] getFieldNames(Object obj) {
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String[] names = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
        }

        return names;
    }

    /**
     * 得到字段数组对应的字段名数组
     * @param fields
     * @return
     */
    public static String[] getFieldNames(Field[] fields) {
        String[] names = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
        }

        return names;
    }


    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getMethodName = getGetMethodName(fieldName);
        final Method method = obj.getClass().getMethod(getMethodName);
        return method.invoke(obj);
    }

    public static Object getHierarchicalFieldValue(Object obj, String fieldNameSequence) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if(fieldNameSequence.indexOf('.')<0){
            return getFieldValue(obj, fieldNameSequence);
        } else {
            String[] fieldNames = fieldNameSequence.split("\\.");

            return getHierarchicalFieldValue(obj, fieldNames);
        }
    }

    public static Object getHierarchicalFieldValue(Object obj, String[] fieldNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object currentObj = obj;
        for(String fieldName : fieldNames) {
            currentObj = getFieldValue(currentObj, fieldName);
        }
        return currentObj;
    }


    private static String getGetMethodName(String fieldName) {
        return "get"+ StringUtil.upperInitial(fieldName);
    }


    /**
     * 得到简单成员变量
     * 1. 非静态
     * 2. 简单类型: int, float, double, String, char
     */
    public static Field[] getSimpleFields(Class clazz) {
        final Field[] fields = clazz.getFields();

        ArrayList<Field> fieldList = new ArrayList<Field>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String type = field.getType().getSimpleName();
                if ("int".equals(type)
                        || "long".equals(type)
                        || "float".equals(type)
                        || "double".equals(type)
                        || "char".equals(type)
                        || "String".equals(type)
                        )
                    fieldList.add(field);
            }
        }

        Field[] resultFields = new Field[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            resultFields[i] = fieldList.get(i);
        }

        return resultFields;
    }



}
