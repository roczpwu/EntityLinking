package com.roc.components.captcha;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * User: rocwu
 * Time: 2016-10-26
 * Time: 12:56
 * Desc: 校验二维码的拦截器
 */
public class CaptchaCodeValidator extends FieldValidatorSupport {

    @Override
    public void validate(Object o) throws ValidationException {

        String fieldName = getFieldName();
        String fieldValue = (String) getFieldValue(fieldName, o);

        ActionContext ac = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);

        System.out.println(fieldValue);
        //addFieldError(fieldName, o);
    }
}
