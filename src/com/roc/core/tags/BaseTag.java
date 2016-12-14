package com.roc.core.tags;

import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.ValueStack;
import com.roc.core.base.BaseAction;
import org.apache.struts2.views.jsp.TagUtils;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: 表单验证标签类的基类
 */
public abstract class BaseTag extends TagSupport {


    /**
     * 从valueStack取Action
     * @return
     */
    public BaseAction getActionFromStack() {
        ValueStack valueStack = TagUtils.getStack(pageContext);
        Map<String, Object> stackMap = valueStack.getContext();
        for (String key : stackMap.keySet()) {
            Object value = stackMap.get(key);
            if (value instanceof BaseAction)
                return (BaseAction) value;
        }
        return null;
    }

    /**
     * 从ValueStack取form到action的映射表
     * 若不存在,则创建一个并插入ValueStack
     * @return
     */
    public FormToActionMap getFormToActionMapFrom() {
        ValueStack valueStack = TagUtils.getStack(pageContext);
        CompoundRoot valueStackRoot = valueStack.getRoot();
        for (Object value : valueStackRoot) {
            if (value instanceof FormToActionMap)
                return (FormToActionMap) value;
        }
        FormToActionMap value = new FormToActionMap();
        valueStackRoot.push(value);
        return value;
    }

}
