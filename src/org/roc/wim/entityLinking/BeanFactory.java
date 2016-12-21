package org.roc.wim.entityLinking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 14:08
 * Desc: spring工厂封装
 */
public class BeanFactory {

    private static ApplicationContext ac = null;

    static {
        ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static Object getBean(String name) {
        return ac.getBean(name);
    }

    public static Object getBean(String name, Object args) {
        return ac.getBean(name, args);
    }
}
