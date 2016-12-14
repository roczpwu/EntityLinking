package com.roc.ajax;
import com.roc.core.BLMessage;
import org.apache.logging.log4j.*;

/**
 * User: rocwu
 * Date: 2016/08/09
 * Time: 1:55
 * Desc: 异步ajax方法类
 */
public class AjaxInterface {

    protected Logger logger = LogManager.getLogger(this.getClass().getName());

    public BLMessage testAjax(int userGroupId, String userIds) {
        return new BLMessage(true,"保存成功", "hello world");
    }

}
