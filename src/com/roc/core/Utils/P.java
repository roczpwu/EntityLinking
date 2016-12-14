package com.roc.core.Utils;

import java.util.Calendar;
import java.util.Stack;

/**
 * User: Lu Timgming
 * Date: 2010-1-8
 * Time: 13:56:45
 * Desc: 打印类
 * 为了使用方便，类名只用一个字母
 */
public class P {
    // 记录开始时间的栈
    public static Stack<Long> startTimeStack = new Stack<Long>();
    // 记录任务的栈
    public static Stack<String> taskStack = new Stack<String>();

    /**
     * 打印
     */
    public static void p(Object obj) {
        System.out.println(obj);
    }

    /**
     * 打印空行
     */
    public static void p() {
        System.out.println();
    }

    /**
     * 先打印TAB，再打印内容
     * @param tabNum
     * @param obj
     */
    public static void p(int tabNum, Object obj) {
        for (int i = 0; i < tabNum; i++) {
            System.out.print('\t');
        }
        System.out.println(obj);
    }


    /**
     * 先打印TAB，再打印ERROR，最后打印内容
     * @param tabNum
     * @param obj
     */
    public static void pe(int tabNum, Object obj) {
        for (int i = 0; i < tabNum; i++) {
            System.out.print('\t');
        }
        System.out.print("ERROR:");
        System.out.println(obj);
    }


    /**
     * 打印，不换行
     * @param obj
     */
    public static void pp(Object obj) {
        System.out.print(obj);
    }


    /**
     * 先打印时间，再打印内容
     * @param obj
     */
    public static void pt(Object obj) {
        System.out.println("["+ TimeUtil.getDate_time_ms() + "] " + obj);
    }


    /**
     * 打印任务开始
     * @param obj
     */
    public static void pstart(Object obj) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        startTimeStack.add(currentTime);
        taskStack.add(obj.toString());
        System.out.println("[" + TimeUtil.getDate_time_ms() + "] START  of " + obj);
    }

    /**
     * 打印任务结束
     */
    public static void pfinish() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long startTime = startTimeStack.pop();
        String task = taskStack.pop();
        System.out.println("[" + TimeUtil.getDate_time_ms() + "] FINISH of " + task);
        System.out.println("DURATION:" + TimeUtil.calcDuration(startTime, currentTime));
    }
}
