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

    public static String implode(Object[] array, String spliter) {
        StringBuilder sb = new StringBuilder();
        if (array == null || array.length == 0) return null;
        for (Object item : array) {
            sb.append(spliter).append(item);
        }
        return sb.substring(spliter.length());
    }

    public static Object explode(String str, String spliter) {
        if (str == null) return null;
        return str.split(spliter);
    }
}
