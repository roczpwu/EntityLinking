package com.roc.core.jqueryValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rocwu
 * Date: 2016/08/19
 * Time: 0:23
 * Desc: 表单字段jquery验证字段类
 */
public class Field {
    private List<FieldValidator> fieldValidatorList = new ArrayList<>();
    private String name;

    public List<FieldValidator> getFieldValidatorList() {
        return fieldValidatorList;
    }

    public void setFieldValidatorList(List<FieldValidator> fieldValidatorList) {
        this.fieldValidatorList = fieldValidatorList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
