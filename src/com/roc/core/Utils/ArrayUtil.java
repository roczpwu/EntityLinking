package com.roc.core.Utils;

/**
 * User: rocwu
 * Date: 2016-10-4
 * Time: 7:10 pm
 * Desc: 字符类型
 */
public class ArrayUtil {

    public static int firstIndexOf(Object[] arr, Object target) {
        if (arr == null || arr.length == 0)
            return -1;
        for (int i = 0; i < arr.length; i++) {
            if (target.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

}
