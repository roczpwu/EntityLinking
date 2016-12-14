package com.roc.core.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User:
 * Lu Tingming
 * Time:
 * 2010-11-11 20:53:54
 * Desc:
 * 时间工具
 */
public class TimeUtil {
    public static char[] CHINESE_WEEKDAYS = new char[]{
            '日', '一', '二', '三', '四', '五', '六', '日'
    };

    private static HashMap<String, Integer> monthMap;

    static {
        monthMap = new HashMap<String, Integer>();
        monthMap.put("January", 1);
        monthMap.put("Jan", 1);
        monthMap.put("JAN", 1);

        monthMap.put("February", 2);
        monthMap.put("Feb", 2);
        monthMap.put("FEB", 2);

        monthMap.put("March", 3);
        monthMap.put("Mar", 3);
        monthMap.put("MAR", 3);

        monthMap.put("April", 4);
        monthMap.put("Apr", 4);
        monthMap.put("APR", 4);

        monthMap.put("May", 5);
        monthMap.put("MAY", 5);

        monthMap.put("June", 6);
        monthMap.put("Jun", 6);
        monthMap.put("JUN", 6);

        monthMap.put("July", 7);
        monthMap.put("Jul", 7);
        monthMap.put("JUL", 7);

        monthMap.put("August", 8);
        monthMap.put("Aug", 8);
        monthMap.put("AUG", 8);

        monthMap.put("September", 9);
        monthMap.put("Sept", 9);
        monthMap.put("Sep", 9);
        monthMap.put("SEP", 9);

        monthMap.put("October", 10);
        monthMap.put("Oct", 10);
        monthMap.put("OCT", 10);

        monthMap.put("November", 11);
        monthMap.put("Nov", 11);
        monthMap.put("NOV", 11);

        monthMap.put("December", 12);
        monthMap.put("Dec", 12);
        monthMap.put("DEC", 12);
    }

    // 各个时间段相差的毫秒数
    public static final long MILLIS_DAY = 1000 * 60 * 60 * 24;
    public static final long MILLIS_MONTH = 1000 * 60 * 60 * 24 * 30L;
    public static final long MILLIS_YEAR = 1000 * 60 * 60 * 24 * 30 * 365L;

    public static String getMM(String monthStr) {
        Integer i = monthMap.get(monthStr);
        if (i == null) {
            return null;
        }
        if (i.intValue() <= 9) {
            return "0" + i;
        } else {
            return i.toString();
        }
    }


    public static List<int[]> getYearMonthList(int yearBegin, int monthBegin, int yearEnd, int monthEnd) {
        int size = (yearEnd - yearBegin + 1) * 12 + (monthEnd - monthBegin + 1);
        List<int[]> list = new ArrayList<int[]>(size);

        int year = yearBegin;
        int month = monthBegin;
        for (int i = 0; i < size; i++) {
            list.add(new int[]{year, month});
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        }

        return list;
    }


    /**
     * yyyy-mm
     *
     * @param dateBegin
     * @param dateEnd
     * @param fieldName
     * @return
     */
    public static String getYearMonthListSql(String dateBegin, String dateEnd, String fieldName) {
        List<int[]> yearMonthList = getYearMonthList(
                Integer.parseInt(dateBegin.substring(0, 4)),
                Integer.parseInt(dateBegin.substring(4, 6)),
                Integer.parseInt(dateEnd.substring(0, 4)),
                Integer.parseInt(dateEnd.substring(4, 6))
        );

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < yearMonthList.size(); i++) {
            int[] yearMonth = yearMonthList.get(i);
            int year = yearMonth[0];
            int month = yearMonth[1];
            String yearMonthString = getYYYY_MM(year, month);
            if (sb.length() > 0) {
                sb.append("UNION ALL\n");
            }
            sb.append("SELECT '").append(yearMonthString).append("' ").append(fieldName).append('\n');
        }

        return sb.toString();
    }


    /**
     * yyyy-mm
     *
     * @param year
     * @param month
     * @return
     */
    public static String getYYYY_MM(int year, int month) {
        return year
                + "-"
                + (month < 10 ? "0" + month : month);
    }


    /**
     * 返回今天的前几天
     *
     * @param days
     * @return
     */
    public static String getDateBefore(int days) {
        Calendar c = getCalendar(getYyyy_mm_dd());
        return getDateByCalendarAndOffset(c, -days);
    }

    /**
     * 返回现在的前几分钟
     *
     * @param minutes
     * @return
     */
    public static Calendar getTimeBeforeMinutes(int minutes) {
        Calendar c = Calendar.getInstance();
        addMinutes(c, -minutes);
        return c;
    }

    /**
     * 返回现在的前几秒钟
     *
     * @param seconds
     * @return
     */
    public static Calendar getTimeBeforeSeconds(int seconds) {
        Calendar c = Calendar.getInstance();
        addSeconds(c, -seconds);
        return c;
    }

    /**
     * 返回指定日期的前几天
     *
     * @param yyyy_mm_dd
     * @param days
     * @return
     */
    public static String getDateBefore(String yyyy_mm_dd, int days) {
        Calendar c;
        if (StringUtil.isEmpty(yyyy_mm_dd)) {
            c = Calendar.getInstance();
        } else {
            c = getCalendar(yyyy_mm_dd);
        }
        return getDateByCalendarAndOffset(c, -days);
    }

    private static String getDateByCalendarAndOffset(Calendar c, int days) {
        c.add(Calendar.DAY_OF_MONTH, days);

        return getYyyy_mm_dd(c);
    }


    private static void addMinutes(Calendar c, int minutes) {
        c.add(Calendar.MINUTE, minutes);
    }

    private static void addSeconds(Calendar c, int seconds) {
        c.add(Calendar.SECOND, seconds);
    }

    /**
     * 返回今天的后几天
     *
     * @return
     */
    public static String getDateAfter(int days) {
        return getDateByCalendarAndOffset(Calendar.getInstance(), days);
    }

    /**
     * 返回指定日期的后几天
     *
     * @param yyyy_mm_dd
     * @param days
     * @return
     */
    public static String getDateAfter(String yyyy_mm_dd, int days) {
        return getDateBefore(yyyy_mm_dd, -days);
    }

    /**
     * 得到指定字符串所指的calendar
     *
     * @param dateTimeString
     * @return
     */
    public static Calendar getCalendar(String dateTimeString) {
        final Calendar instance;
        if (dateTimeString.length() == 8) {
            // yyyymmdd
            instance = Calendar.getInstance();
            instance.set(Calendar.YEAR, Integer.parseInt(dateTimeString.substring(0, 4)));
            instance.set(Calendar.MONTH, Integer.parseInt(dateTimeString.substring(4, 6)) - 1);
            instance.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateTimeString.substring(6, 8)));
        } else if (dateTimeString.length() == 10) {
            // yyyy-mm-dd
            String[] values = dateTimeString.split("-");
            instance = Calendar.getInstance();
            instance.set(Calendar.YEAR, Integer.parseInt(values[0]));
            instance.set(Calendar.MONTH, Integer.parseInt(values[1]) - 1);
            instance.set(Calendar.DAY_OF_MONTH, Integer.parseInt(values[2]));
        } else {
            // yyyy-mm-dd hh:mm:ss
            if (dateTimeString.length() > 19) {
                dateTimeString = dateTimeString.substring(0, 19);
            }
            String[] values = dateTimeString.split("[-, ,:]");
            instance = Calendar.getInstance();
            instance.set(Calendar.YEAR, Integer.parseInt(values[0]));
            instance.set(Calendar.MONTH, Integer.parseInt(values[1]) - 1);
            instance.set(Calendar.DAY_OF_MONTH, Integer.parseInt(values[2]));
            instance.set(Calendar.HOUR_OF_DAY, Integer.parseInt(values[3]));
            instance.set(Calendar.MINUTE, Integer.parseInt(values[4]));
            instance.set(Calendar.SECOND, Integer.parseInt(values[5]));
        }

        return instance;
    }

    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String getYyyy_mm_dd(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return formatYyyy_mm_dd(year, month + 1, dayOfMonth);
    }

    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String formatYyyy_mm_dd_hh_mm_ss(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return formatYyyy_mm_dd_hh_mm_ss(year, month + 1, dayOfMonth,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND)
        );
    }


    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String formatYyyy_mm_dd_hh_mm(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return formatYyyy_mm_dd_hh_mm(
                year, month + 1, dayOfMonth,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE)
        );
    }


    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String getYyyy_mm_dd_hh_mm() {
        return getYyyy_mm_dd() + "_" + getCurrent_hh_mm();
    }


    /**
     * 得到日期，形如：2010-12-20
     */
    public static String getYyyy_mm_dd_hh() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return getYyyy_mm_dd() + "_"
                + (h >= 10 ? String.valueOf(h) : ("0" + h));
    }


    /**
     * 得到日期，形如：2010-12-20 12:34:56
     */
    public static String getYyyy_mm_dd_hh_mm_ss() {
        return formatYyyy_mm_dd_hh_mm_ss(Calendar.getInstance());
    }


    public static int getCurrentYear() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.YEAR);
    }


    public static int getCurrentMonth() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.MONTH) + 1;
    }


    public static int getCurrentDate_of_month() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 返回当前小时（0-23）
     */
    public static int getCurrentHour() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.HOUR_OF_DAY);
    }


    public static int getCurrentMinute() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.MINUTE);
    }


    public static int getCurrentSecond() {
        Calendar cld = Calendar.getInstance();
        return cld.get(Calendar.SECOND);
    }

    /**
     * 得到日期，形如：2010-12-20
     */
    public static String getYyyy_mm_dd() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return formatYyyy_mm_dd(year, month + 1, dayOfMonth);
    }

    /**
     * 原形式29 March 2013
     */
    public static String getYyyy_mm_ddV1(String date) {
        String[] vec = date.trim().split(" +");
        String mm = getMM(vec[1]);
        String dd = vec[0].trim();
        String year = vec[2].trim();
        return year + "-" + mm + "-" + dd;
    }

    /**
     * 原形式：May 28, 2012
     * 得到日期，形如：2010-12-20
     *
     * @param date
     * @return
     */
    public static String getYyyy_mm_dd(String date) {
        if (date.contains(","))
            date = date.replace(",", "");
        if (date.contains(" "))
            date = date.replace(" ", " ");
        //此处用split(" +")来代替原来的split(" ") 因为单词之间可能不止一个空格 用split(" ")为出错，如果中间有多个空格，可以使用" +" 来进行分割
        String[] vec = date.trim().split(" +");
        String mm = getMM(vec[0]);
        String dd = vec[1].trim();
        if (dd.length() == 1) {
            dd = "0" + dd;
        }
        return vec[2].trim() + "-" + mm + "-" + dd;
    }

    /**
     * 01/05/2009 月/日/年
     *
     * @param date
     * @return
     */
    public static String getYyyy_mm_ddFrommm_dd_yyyy(String date) {
        String yyyy = "";
        String mm = "";
        String dd = "";
        if (!date.contains("/")) {
            if (date.contains(","))
                date = date.replace(",", "");
            String[] vec = date.trim().split("\\s+");
            dd = vec[0].trim();
            if (dd.length() == 1) {
                dd = "0" + dd;
            }
            mm = getMM(vec[1].trim());
            yyyy = vec[2].trim();

        } else {
            //01/05/2009 月/日/年
            String[] vec = date.trim().split("/");
            mm = vec[0].trim();
            dd = vec[1].trim();
            yyyy = vec[2].trim();

        }
        return yyyy + "-" + mm + "-" + dd;

    }

    /**
     * 2009/07/10
     *
     * @param date
     * @return
     */
    public static String getYyyy_mm_ddFromyyyy_mm_dd(String date) {
        String time = date.replace("/", "-");
        return time;

    }

    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String getYyyy_mm_dd_yesterday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return TimeUtil.getYyyy_mm_dd(calendar);
    }


    /**
     * 得到日期，形如：2010-12-20
     *
     * @return
     */
    public static String getYyyy_mm_dd_tomorrow() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return TimeUtil.getYyyy_mm_dd(calendar);
    }


    /**
     * 得到日期，形如：20101220
     *
     * @return
     */
    public static String getYyyyMMdd() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        return formatYyyyMMdd(year, month + 1, dayOfMonth);
    }

    /**
     * 得到日期时间，形如：2010-12-20 13:57:18
     *
     * @return
     */
    public static String getDateTimeWithFormat() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        return getYyyy_mm_dd()
                + " " + (hh < 10 ? "0" + hh : String.valueOf(hh))
                + ":" + (mm < 10 ? "0" + mm : String.valueOf(mm))
                + ":" + (ss < 10 ? "0" + ss : String.valueOf(ss));
    }

    /**
     * 得到日期时间，形如：20101220135718
     *
     * @return
     */
    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        return getYyyyMMdd()
                + (hh < 10 ? "0" + hh : String.valueOf(hh))
                + (mm < 10 ? "0" + mm : String.valueOf(mm))
                + (ss < 10 ? "0" + ss : String.valueOf(ss));
    }

    /**
     * 格式化秒数，形如：13:57:18
     *
     * @param duration
     * @return
     */
    public static String formatSeconds(long duration) {
        long seconds = duration % 60;
        duration /= 60;

        long minutes = duration % 60;
        duration /= 60;

        long hours = duration % 60;
        duration /= 60;

        return (hours < 10 ? "0" + hours : String.valueOf(hours))
                + ":" + (minutes < 10 ? "0" + minutes : String.valueOf(minutes))
                + ":" + (seconds < 10 ? "0" + seconds : String.valueOf(seconds));
    }

    /**
     * 计算时长
     *
     * @param start
     * @param finish
     * @return
     */
    public static String calcDuration(long start, long finish) {
        long duration = finish - start;

        long milliSeconds = duration % 1000;
        duration /= 1000;

        String s_milliSeconds;
        if (milliSeconds < 10) {
            s_milliSeconds = "00" + milliSeconds;
        } else if (milliSeconds < 100) {
            s_milliSeconds = "0" + milliSeconds;
        } else {
            s_milliSeconds = "" + milliSeconds;
        }
        String durationInSecond = duration + "." + s_milliSeconds;

        long seconds = duration % 60;
        duration /= 60;

        long minutes = duration % 60;
        duration /= 60;

        long hours = duration % 60;
        duration /= 60;

        String s_ms;
        if (milliSeconds < 10) {
            s_ms = "00" + milliSeconds;
        } else if (milliSeconds < 100) {
            s_ms = "0" + milliSeconds;
        } else {
            s_ms = "" + milliSeconds;
        }

        return (hours < 10 ? "0" + hours : String.valueOf(hours))
                + ":" + (minutes < 10 ? "0" + minutes : String.valueOf(minutes))
                + ":" + (seconds < 10 ? "0" + seconds : String.valueOf(seconds))
                + "." + s_ms
                + "(" + durationInSecond + " seconds)";
    }

    /**
     * 计算时长
     *
     * @param start
     * @return
     */
    public static String calcDurationInSeconds(long start) {
        return calcDurationInSeconds(start, Calendar.getInstance().getTimeInMillis());
    }

    /**
     * 计算时长
     *
     * @param start
     * @param finish
     * @return
     */
    public static String calcDurationInSeconds(long start, long finish) {
        long duration = finish - start;

        long milliSeconds = duration % 1000;
        duration /= 1000;

        String s_milliSeconds;
        if (milliSeconds < 10) {
            s_milliSeconds = "00" + milliSeconds;
        } else if (milliSeconds < 100) {
            s_milliSeconds = "0" + milliSeconds;
        } else {
            s_milliSeconds = "" + milliSeconds;
        }

        return duration + "." + s_milliSeconds;
    }


    /**
     * 得到日期时间，形如：20101220_135718
     *
     * @return
     */
    public static String getDate_Time() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        return
                year
                        + (month < 10 ? "0" + month : String.valueOf(month))
                        + (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth))
                        + "_" + (hh < 10 ? "0" + hh : String.valueOf(hh))
                        + (mm < 10 ? "0" + mm : String.valueOf(mm))
                        + (ss < 10 ? "0" + ss : String.valueOf(ss));
    }


    /**
     * 得到日期时间，形如：2010-12-20 13:57:18.123
     *
     * @return
     */
    public static String getDate_time_ms() {
        Calendar c = Calendar.getInstance();
        return getDate_time_ms(c);
    }


    /**
     * 得到日期时间，形如：2010-12-20 13:57:18.123
     *
     * @return
     */
    public static String getDate_time_ms(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        return formatYyyy_mm_dd_hh_mm_ss_ms(year, month, dayOfMonth, hh, mm, ss, ms);
    }

    /**
     * 得到日期时间，形如：20101220135718123
     *
     * @return
     */
    public static String getDateTimeMs() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        int ms = c.get(Calendar.MILLISECOND);
        return
                year
                        + (month < 10 ? "0" + month : String.valueOf(month))
                        + (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth))
                        + (hh < 10 ? "0" + hh : String.valueOf(hh))
                        + (mm < 10 ? "0" + mm : String.valueOf(mm))
                        + (ss < 10 ? "0" + ss : String.valueOf(ss))
                        + (ms < 10 ? "00" + ms : (ms < 100 ? "0" + ms : String.valueOf(ms)));
    }

    /**
     * 格式化日期，形如：2010-12-20
     */
    public static String formatYyyy_mm_dd(int year, int month, int dayOfMonth) {
        return
                year + "-" +
                        (month < 10 ? "0" + month : String.valueOf(month)) + "-" +
                        (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth));
    }

    /**
     * 格式化日期，形如：2010-12-20 13:01:02
     */
    public static String formatYyyy_mm_dd_hh_mm_ss(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return
                year + "-" +
                        (month < 10 ? "0" + month : String.valueOf(month)) + "-" +
                        (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth)) + " " +
                        (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" +
                        (minute < 10 ? "0" + minute : String.valueOf(minute)) + ":" +
                        (second < 10 ? "0" + second : String.valueOf(second))
                ;
    }


    /**
     * 格式化日期，形如：2010-12-20 13:01:02.003
     */
    public static String formatYyyy_mm_dd_hh_mm_ss_ms(int year, int month, int dayOfMonth, int hour, int minute, int second, int ms) {
        return formatYyyy_mm_dd_hh_mm_ss(year, month, dayOfMonth, hour, minute, second)
                + "." + (ms < 10 ? "00" + ms : (ms < 100 ? "0" + ms : String.valueOf(ms)));
    }


    /**
     * 格式化日期，形如：2010-12-20 13:01
     */
    public static String formatYyyy_mm_dd_hh_mm(int year, int month, int dayOfMonth, int hour, int minute) {
        return
                year + "-" +
                        (month < 10 ? "0" + month : String.valueOf(month)) + "-" +
                        (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth)) + " " +
                        (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" +
                        (minute < 10 ? "0" + minute : String.valueOf(minute))
                ;
    }


    /**
     * 格式化日期，形如：15:20
     *
     * @return
     */
    public static String format_hh_mm(int hour, int minute) {
        return (hour < 10 ? "0" + hour : String.valueOf(hour)) + ":" +
                (minute < 10 ? "0" + minute : String.valueOf(minute));
    }


    /**
     * 格式化日期，形如：15:20
     *
     * @return
     */
    public static String getCurrent_hh_mm() {
        return format_hh_mm(getCurrentHour(), getCurrentMinute());
    }


    /**
     * 格式化日期，形如：20101220
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static String formatYyyyMMdd(int year, int month, int dayOfMonth) {
        return
                year +
                        (month < 10 ? "0" + month : String.valueOf(month)) +
                        (dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth));
    }


    /**
     * 将字符串（yyyyMMdd）转换成日期
     * 如果有异常，返回空
     *
     * @param s
     * @return
     */
    public static Date yyyyMMddToDate(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            return format.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串（yyyyMMddHHmm）转换成日期
     * 如果有异常，返回空
     *
     * @param s
     * @return
     */
    public static Date yyyyMMddHHmmToDate(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 计算天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calcDays(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
    }


    /**
     * 计算天数
     */
    public static int calcDays(String startDate, String endDate) {
        return (int) (
                (getCalendar(endDate).getTimeInMillis() - getCalendar(startDate).getTimeInMillis())
                        / (1000 * 3600 * 24)
        );
    }


    /**
     * 计算今天到指定日期的天数
     * 如果指定日期小于今天，那么返回负值
     */
    public static int calcDays(String date) {
        return (int) (
                (getCalendar(getYyyy_mm_dd()).getTimeInMillis() - getCalendar(date).getTimeInMillis())
                        / (1000 * 3600 * 24)
        );
    }


    /**
     * 计算两天之间的间隔天数的绝对值
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int calcAbsDays(String firstDate, String secondDate){
        int interval=calcDays(firstDate,secondDate);
        return Math.abs(interval);
    }


    /**
     * @return
     */
    public static String dateTimeToDate(String dateTime) {
        if (dateTime == null || dateTime.length() <= 10) {
            return dateTime;
        } else {
            return dateTime.substring(0, 10);
        }
    }


    public static int getLastDayOfMonth(String yyyy_mm_dd) {
        String[] as = new String[3] ;
        // 如果是2013-06-13的形式
        if (yyyy_mm_dd.contains("-")) {
            as = yyyy_mm_dd.split("-");
        } else { // 如果是20130613的形式
            as[0] = yyyy_mm_dd.substring(0, 4);
            as[1] = yyyy_mm_dd.substring(4, 6);
        }

        return getLastDayOfMonth(Integer.parseInt(as[0]), Integer.parseInt(as[1]));

    }
    public static int getLastDayOfMonth(int year, int month) {
        Calendar cld = Calendar.getInstance();
        cld.set(year, toCalendarMonth(month), 1);
        return cld.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static int toCalendarMonth(int month) {
        if (month == 0) {
            return Calendar.DECEMBER;
        } else {
            return month - 1;
        }
    }


    /**
     * 从
     * 2012-07-23 14:44:33.330 转化为
     * 2012-07-23 14:44
     *
     * @param timeString
     * @return
     */
    public static String formatYYYYMMDD_HHmm(String timeString) {
        if (timeString == null) {
            return "";
        } else if (timeString.length() < 16) {
            return timeString;
        } else {
            return timeString.substring(0, 16);
        }
    }

    /**
     * 根据日期差，显示时间
     *
     * @param timeString
     * @return
     */
    public static String getRelateTimeToNow(String timeString) {
        if (timeString == null)
            return "";
        // 计算相对时间
        Date now = new Date();
        Date actTime = TimeUtil.yyyyMMddHHmmToDate(timeString);
        long timeDelta = now.getTime() - actTime.getTime();
        Calendar cnow = Calendar.getInstance(), cact = Calendar.getInstance();
        cnow.setTime(now);
        cact.setTime(actTime);
        int dayDelta = cnow.get(Calendar.DAY_OF_MONTH) - cact.get(Calendar.DAY_OF_MONTH);

        if (timeDelta < MILLIS_DAY && dayDelta < 1) {
            return timeString.substring(11, 19);
        } else if (timeDelta < MILLIS_MONTH) {
            return timeString.substring(5, 16);
        } else {
            return timeString.substring(0, 10);
        }
    }

    //16th November 2010 - National Review Online

    public static String getYyyy_mm_ddFromddth_mm_yyyy(String date) {
        String[] vec = date.split("\\s+");
        if (vec.length < 3) {
            return "";
        }
        String yyyy = vec[2];
        String mm = getMM(vec[1]);
        String dd = "";
        if (vec[0].contains("th"))
            dd = vec[0].replace("th", "").trim();
        else if (vec[0].contains("st"))
            dd = vec[0].replace("st", "").trim();
        else if (vec[0].contains("nd"))
            dd = vec[0].replace("nd", "").trim();
        else if (vec[0].contains("rd"))
            dd = vec[0].replace("rd", "").trim();
        if (dd.length() == 1)
            dd = "0" + dd;
        return yyyy + "-" + mm + "-" + dd;
    }

    /**
     * 23 Oct 2012
     * 23 October 2012
     *
     * @param date
     * @return
     */
    public static String getYyyy_mm_ddFromdd_mm_yyyy(String date) {
        if (date.contains(","))
            date = date.replace(",", "");
        String[] vec = date.split("\\s+");
        String dd = vec[0].trim();
        if (dd.length() == 1)
            dd = "0" + dd;
        String mm = getMM(vec[1].trim());
        String yyyy = vec[2].trim();
        return yyyy + "-" + mm + "-" + dd;
    }

    /**
     * July 19th, 2012
     *
     * @param date
     * @return
     */
    public static String getYyyy_mm_ddFrommm_ddth_yyyy(String date) {
        if (date.contains(","))
            date = date.replace(",", " ");
        String[] vec = date.split("\\s+");
        String mm = getMM(vec[0].trim());
        String dd = "";
        if (vec[1].contains("th"))
            dd = vec[1].replace("th", "").trim();
        else if (vec[1].contains("st"))
            dd = vec[1].replace("st", "").trim();
        else if (vec[1].contains("nd"))
            dd = vec[1].replace("nd", "").trim();
        else if (vec[1].contains("rd"))
            dd = vec[1].replace("rd", "").trim();
        if (dd.length() == 1)
            dd = "0" + dd;
        return vec[2] + "-" + mm + "-" + dd;
    }


    /**
     * 计算指定时间到现在的毫秒数
     */
    public static long getDuration(String dateTimeString0, String dateTimeString1) {
        Calendar cld_dateTime0 = getCalendar(dateTimeString0);
        Calendar cld_dateTime1 = getCalendar(dateTimeString1);

        return cld_dateTime1.getTimeInMillis() - cld_dateTime0.getTimeInMillis();
    }


    /**
     * 计算指定时间到现在的毫秒数
     */
    public static long getDurationMSToNow(String dateTimeString) {
        Calendar cld_dateTime = getCalendar(dateTimeString);
        Calendar cld_now = Calendar.getInstance();

        return cld_now.getTimeInMillis() - cld_dateTime.getTimeInMillis();
    }


    /**
     * 计算指定时间到现在的秒数
     */
    public static long getDurationToNowInSecond(String dateTimeString) {
        return getDurationMSToNow(dateTimeString) / 1000;
    }


    /**
     * 计算指定时间到现在的分钟数
     */
    public static double getDurationToNowInMinute(String dateTimeString) {
        return getDurationMSToNow(dateTimeString) * 1.0 / (1000 * 60);
    }


    /**
     * 得到时间间隔（以毫秒为单位）
     *
     * @return
     */
    public static int getIntervalInMS(int interval, char unit) throws Exception {
        int ratio;
        if (unit == 'd') {
            ratio = 24 * 60 * 60 * 1000;
        } else if (unit == 'h') {
            ratio = 60 * 60 * 1000;
        } else if (unit == 'm') {
            ratio = 60 * 1000;
        } else {
            throw new Exception("Unrecognized update interval unit: " + unit);
        }

        return ratio * interval;
    }

    /**
     * 指定时间到现在，是否已经超过时间间隔
     *
     * @return
     */
    public static boolean isExceeded(String lastDateTime, int interval, char intervalUnit) throws Exception {
        int intervalInMS = getIntervalInMS(interval, intervalUnit);
        long intervalToNow = getDurationMSToNow(lastDateTime);

        return intervalToNow > intervalInMS;
    }


    /**
     * 得到两个日期之间的日期列表
     */
    public static List<String> getDateListBetween(String dateBegin, String dateEnd) {
        List<String> dateList = new ArrayList<String>();
        for (String currentDate = dateBegin;
             currentDate.compareTo(dateEnd) <= 0;
             currentDate = TimeUtil.getDateAfter(currentDate, 1)) {
            dateList.add(currentDate);
        }

        return dateList;
    }


    /**
     * 是否达到或超过指定时间
     */
    public static boolean hasReached(int hour, int minute) {
        int currentHour = getCurrentHour();
        if (currentHour > hour) {
            return true;
        } else if (currentHour == hour) {
            return getCurrentMinute() >= minute;
        } else {
            return false;
        }
    }


    public static String addHyphen(String date) {
        if(StringUtil.notEmpty(date)&&date.length() == 8) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
        } else {
            return date;
        }
    }


    /**
     * 得到指定日期所在星期的第一天
     */
    public static String getFirstDateOfTheWeek() {
        return getFirstDateOfTheWeek(getYyyy_mm_dd());
    }

    /**
     * 得到指定日期所在星期的第一天
     */
    public static String getFirstDateOfTheWeek(String date) {
        Calendar cld = getCalendar(date);
        int dayOfWeek = cld.get(Calendar.DAY_OF_WEEK);
        int offset = (7 + dayOfWeek - Calendar.MONDAY) % 7;
        cld.add(Calendar.DAY_OF_MONTH, -offset);
        return getYyyy_mm_dd(cld);
    }

    /**
     * 得到指定日期所在星期的最后一天
     */
    public static String getLastDateOfTheWeek(String date) {
        String firstDateOfTheWeek = getFirstDateOfTheWeek(date);
        return getDateAfter(firstDateOfTheWeek, 6);
    }

    /**
     * 得到指定日期所在星期的最后一天
     */
    public static String getLastDateOfTheWeek() {
        String firstDateOfTheWeek = getFirstDateOfTheWeek(getYyyy_mm_dd());
        return getDateAfter(firstDateOfTheWeek, 6);
    }

    /**
     * 得到今天所在月的第一天
     */
    public static String getFirstDateOfTheMonth() {
        return getFirstDateOfTheMonth(getYyyy_mm_dd());
    }


    /**
     * 得到指定日期所在月的第一天
     */
    public static String getFirstDateOfTheMonth(String date) {
        // 如果date的形式是2013-06-14
        if (date.contains("-")) {
            date = date.substring(0, 8) + "01";
        } else { // 如果date的形式是20130614
            date = date.substring(0, 6) + "01";
        }
        return date;
    }


    /**
     * 得到指定日期所在月的最后一天
     */
    public static String getLastDateOfTheMonth(String date) {
        int lastDayOfMonth = getLastDayOfMonth(date);
        // 如果date的形式是2013-06-14
        if (date.contains("-")) {
            date = date.substring(0, 8) + lastDayOfMonth;
        } else { // 如果date的形式是20130614
            date = date.substring(0, 6) + lastDayOfMonth;
        }
        return date;
    }


    /**
     * 得到中文星期几
     */
    public static char getDayOfWeekInChinese(int weekday) {
        return CHINESE_WEEKDAYS[weekday];
    }

    /**
     * 得到中文星期几
     */
    public static char getDayOfWeekInChinese() {
        return CHINESE_WEEKDAYS[getDayOfWeek()];
    }


    /**
     * 得到星期几
     * 周日是0，周一是1，以此类推
     */
    public static int getDayOfWeek() {
        Calendar cld = Calendar.getInstance();
        int dayOfWeek = cld.get(Calendar.DAY_OF_WEEK) - 1;

        return dayOfWeek;
    }

    /**
     * 去掉时间后面的毫秒
     * @param time_ms
     * @return
     */
    public static String removeMilliSecond(String time_ms){
        if(StringUtil.notEmpty(time_ms)){
            if(time_ms.length() == 10 || time_ms.length() ==21){
                time_ms=time_ms.substring(0, time_ms.length()-2);
            }
        }
        return time_ms;
    }

    /**
     * 规格化时间格式 去掉毫秒 将null替换为“”
     * @param timeStr
     * @return
     */
    public static String formatTime(String timeStr){
        timeStr=removeMilliSecond(timeStr);
        if(StringUtil.isEmpty(timeStr)){
            return "";
        } else{
            return timeStr;
        }
    }

    /**
     * wzp.newsML.MailSample
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println(removeMilliSecond("2014-01-16 16:05:07.0"));
        System.out.println(removeMilliSecond("16:05:07.0"));

    }
}

