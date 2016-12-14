package com.roc.core.jqueryValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: 表单字段jquery验证基类
 */
public class FieldValidator {

    protected List<ValidatorParam> paramList = new ArrayList<>();
    protected String message;
    protected ValidatorType type;

}