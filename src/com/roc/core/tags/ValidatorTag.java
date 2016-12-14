package com.roc.core.tags;

import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.roc.core.Utils.StringUtil;
import com.roc.core.base.BaseAction;
import com.roc.core.jqueryValidation.Validation;
import org.apache.struts2.dispatcher.Dispatcher;

import javax.servlet.jsp.*;
import java.io.IOException;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: js表单验证标签类
 *       该标签需要配合w:form使用
 */
public class ValidatorTag extends BaseTag {

    private String formId;
    private String action;

    @Override
    public int doStartTag() throws JspException {
        this.action = this.getFormToActionMapFrom().getForm2action().get(this.formId);
        JspWriter out=pageContext.getOut();
        try {
            out.println("$('#"+this.formId+"').bootstrapValidator({");
            out.println("\tmessage: 'This value is not valid',");
            out.println("\tfeedbackIcons: {");
            out.println("\t\tvalid: 'glyphicon glyphicon-ok',");
            out.println("\t\tinvalid: 'glyphicon glyphicon-remove',");
            out.println("\t\tvalidating: 'glyphicon glyphicon-refresh'");
            out.println("\t},");
            out.println(getFieldValidatorJs()[1]);
            out.println("});");
            out.println(getFieldValidatorJs()[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out=pageContext.getOut();

        return EVAL_PAGE;
    }

    private ActionInfo getActionInfo() {
        if (StringUtil.isEmpty(this.action))
            return null;
        String actionPath = this.action;
        String namespace = null;
        String action;
        int lastSlashPos = actionPath.lastIndexOf('/');
        if (lastSlashPos>=0) {
            namespace = actionPath.substring(0, lastSlashPos);
        }
        if (StringUtil.isEmpty(namespace)) namespace = "/";
        action = actionPath.substring(lastSlashPos+1);
        int dotPos = action.indexOf('.');
        if (dotPos>=0) {
            action = action.substring(0, dotPos);
        }
        Dispatcher dispatcher = Dispatcher.getInstance();
        ActionConfig actionConfig = dispatcher.getConfigurationManager()
                .getConfiguration().getRuntimeConfiguration()
                .getActionConfig(namespace,action);
        if (actionConfig == null) return null;
        String actionBeanID = actionConfig.getClassName();
        String actionMethodName = actionConfig.getMethodName();
        if (StringUtil.isEmpty(actionMethodName)) actionMethodName = "execute";
        BaseAction baseAction = getActionFromStack();
        Class<?> classType = baseAction.getApplicationContext().getType(actionBeanID);
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setName(action);
        actionInfo.setMethodName(actionMethodName);
        actionInfo.setClassType(classType);

        return actionInfo;
    }

    private String[] getFieldValidatorJs() {
        ActionInfo actionInfo = getActionInfo();
        return Validation.generateValidationJs(actionInfo);
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
