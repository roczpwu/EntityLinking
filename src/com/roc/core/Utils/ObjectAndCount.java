package com.roc.core.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-9-12
 * Time: 0:02:26
 * To change this template use File | Settings | File Templates.
 */
public class ObjectAndCount {
    public Object object;
    public int count;

    public ObjectAndCount(Object object) {
        this.object = object;
        count = 1;
    }

    public ObjectAndCount(Object object, int count) {
        this.object = object;
        this.count = count;
    }
}
