package com.roc.core.tags;

/**
 * User: rocwu
 * Date: 2016/08/14
 * Time: 12:31
 * Desc: ActionInfo类
 */
public class ActionInfo {

    private String name;
    private String methodName;
    private Class<?> classType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }
}
