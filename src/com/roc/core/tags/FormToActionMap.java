package com.roc.core.tags;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/08/13
 * Time: 10:19
 * Desc: ValueStack中的Form到Action映射
 */
public class FormToActionMap {

    private Map<String, String> form2action = new HashMap<>();

    public Map<String, String> getForm2action() {
        return form2action;
    }

    public void setForm2action(Map<String, String> form2action) {
        this.form2action = form2action;
    }
}
