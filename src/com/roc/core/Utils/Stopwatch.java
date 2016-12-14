package com.roc.core.Utils;

import java.util.Calendar;

/**
 * User: Lu Tingming
 * Time: 2010-11-11 20:53:54
 * Desc: 计时器
 */
public class Stopwatch {
    private long startTime;
    private long endTime;

    public Stopwatch(){
        this.startTime = Calendar.getInstance().getTimeInMillis();
    }

    public void start(){
        this.startTime = Calendar.getInstance().getTimeInMillis();
    }

    public void stop(){
        endTime = Calendar.getInstance().getTimeInMillis();
    }

    public double getSeconds(){
        return (endTime - startTime)/1000.0;
    }

    public long getMilliSeconds(){
        return endTime - startTime; 
    }

    public String stopAndGetFormattedTime() {
        stop();
        return TimeUtil.calcDuration(startTime, endTime);
    }

    public double stopAndGetSeconds() {
        stop();
        return getSeconds();
    }

    public double stopAndGetMilliSeconds() {
        stop();
        return getMilliSeconds();
    }
}
