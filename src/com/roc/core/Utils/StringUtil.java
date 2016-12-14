package com.roc.core.Utils;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * User: rocwu
 * Date: 2016-07-16
 * Time: 15:45:03
 * Desc: 与String相关的常用工具类
 */
public class StringUtil {
    public static String getMd5(String ori_str) throws Exception {
        String str = ori_str;
        byte [] buf = str.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        byte [] tmp = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b:tmp) {
            String hexStr = Integer.toHexString(b&0xff);
            if (hexStr.length()<2) hexStr="0"+hexStr;
            sb.append(hexStr);
        }
        return sb.toString();
    }

    /**
     * 抽取出所有符合条件的字符串
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @param afterPattern
     * @return
     */
    public static ArrayList<String> extract(String s, String sBegin, String sEnd, String afterPattern) {
        int beginIdx = s.indexOf(afterPattern) + afterPattern.length();
        return extract(s.substring(beginIdx), sBegin, sEnd);
    }

    /**
     * 抽取出第一个符合条件的字符串
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @return
     */
    public static String extractFirst(String s, String sBegin, String sEnd) {
        int ib;
        ib = s.indexOf(sBegin);
        if (ib < 0) return null;

        int ie;
        ie = s.indexOf(sEnd, ib + sBegin.length());
        // 如果没有找到则跳出循环
        if (ie < 0) return null;

        return s.substring(ib + sBegin.length(), ie);
    }

    /**
     * 抽取第一行
     */
    public static String extractFirstLine(String s) {
        return extractBefore(s, "\n");
    }

    /**
     * 抽取前n行
     */
    public static String extractFirstNLines(String s, int n) {
        StringBuilder sb = new StringBuilder();
        int lastIdx = -1;
        for (int i=0; i<n; i++) {
            int idx = s.indexOf('\n', lastIdx+1);
            if(idx<0) {
                break;
            }
            sb.append(s.substring(lastIdx+1, idx+1));
            lastIdx = idx;
        }
        return sb.toString();
    }

    /**
     * 抽取出某个字符串之前的字符串
     */
    public static String extractBefore(String s, String sEnd) {
        int ie;
        ie = s.indexOf(sEnd);
        // 如果没有找到则跳出循环
        if (ie < 0) return null;

        return s.substring(0, ie);
    }

    /**
     * 抽取出符合条件的哦最后一个字符串
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @return
     */
    public static String extractLast(String s, String sBegin, String sEnd) {
        int ie;
        ie = s.lastIndexOf(sEnd);
        // 如果没有找到则退出
        if (ie < 0) return null;

        int ib;
        ib = s.lastIndexOf(sBegin, ie - sEnd.length());
        if (ib < 0) return null;

        return s.substring(ib + sBegin.length(), ie);
    }

    /**
     * 抽取出符合条件的最后一个字符串
     *
     * @param s
     * @param sBegin
     * @return
     */
    public static String extractFromLast(String s, String sBegin) {
        int ib;
        ib = s.lastIndexOf(sBegin);
        // 如果没有找到则退出
        if (ib < 0) return null;

        return s.substring(ib + sBegin.length());
    }


    /**
     * 抽取出指定字符串，以及该字符串后面的字符串
     *
     * @param s
     * @param sBegin
     * @return
     */
    public static String extractFrom(String s, String sBegin) {
        int ib;
        ib = s.indexOf(sBegin);
        if (ib < 0) return null;

        return s.substring(ib);
    }


    /**
     * 抽取出指定字符串后面的字符串
     *
     * @param s
     * @param sBegin
     * @return
     */
    public static String extractFromExclude(String s, String sBegin) {
        int ib;
        ib = s.indexOf(sBegin);
        if (ib < 0) return null;

        return s.substring(ib + sBegin.length());
    }

    /**
     * 抽取出符合条件的第一个字符串
     *
     * @param s
     * @param p
     * @return
     */
    public static String extractFirst(String s, Pattern p) {
        Matcher matcher = p.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * 抽取出所有符合条件的字符串
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @return
     */
    public static ArrayList<String> extract(String s, String sBegin, String sEnd) {
        ArrayList<String> list = new ArrayList<String>();

        int ib;
        int ie = -1;

        while (true) {
            ib = s.indexOf(sBegin, ie + 1);
            // 如果没有找到则跳出循环
            if (ib < 0) break;

            ie = s.indexOf(sEnd, ib + sBegin.length());
            // 如果没有找到则跳出循环
            if (ie < 0) break;

            list.add(s.substring(ib + sBegin.length(), ie));
        }

        return list;
    }

    /**
     * 从html中抽取出文本
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @param afterPattern
     * @return
     */
    public static ArrayList<String> extractHtml(String s, String sBegin, String sEnd, String afterPattern) {
        int beginIdx = s.indexOf(afterPattern) + afterPattern.length();
        return extractHtml(s.substring(beginIdx), sBegin, sEnd);
    }

    /**
     * 从html中抽取出文本
     *
     * @param s
     * @param sBegin
     * @param sEnd
     * @return
     */
    public static ArrayList<String> extractHtml(String s, String sBegin, String sEnd) {
        s = s.replace("&nbsp;", " ");
        s = s.replace("&amp;", "&");
        s = s.replace("&quot;", "\"");

        return extract(s, sBegin, sEnd);
    }

    /**
     * 格式化字符串，从左边填充
     */
    public static String leftFill(int i, int length, char filler) {
        return leftFill(String.valueOf(i), length, filler);
    }


    /**
     * 格式化字符串，从左边填充
     */
    public static String leftFill(String s, int length, char filler) {
        StringBuffer sb = new StringBuffer();
        for (int i = s.length(); i < length; i++) {
            sb.append(filler);
        }
        sb.append(s);

        return sb.toString();
    }

    /**
     * 格式化字符串，从右边填充
     */
    public static String rightFill(String s, int length, char filler) {
        StringBuffer sb = new StringBuffer();
        sb.append(s);
        for (int i = s.length(); i < length; i++) {
            sb.append(filler);
        }

        return sb.toString();
    }

    /**
     * 格式化数字
     */
    public static String formatNumber(int number, int length, char filler) {
        final String s_number = String.valueOf(number);
        StringBuffer sb = new StringBuffer();
        for (int i = s_number.length(); i < length; i++) {
            sb.append(filler);
        }
        sb.append(s_number);

        return sb.toString();
    }

    /**
     * 获得空格字符串
     */
    public static String getSpaces(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(' ');
        }

        return sb.toString();
    }

    /**
     * 获得字符串
     */
    public static String getChars(int length, char fillerChar) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(fillerChar);
        }

        return sb.toString();
    }

    /**
     * 格式化数字
     */
    public static String formatNumber(int number, int length) {
        return formatNumber(number, length, '0');
    }

    /**
     * 判断是否是常规字符串
     * 条件如下：
     * 小写后面无大写
     * 全字母
     * 第二个大写后不能有小写（如XYz）
     *
     * @param s
     * @return
     */
    public static boolean regular(String s) {
        // 全字母或
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            //if((c>='a' && c<='z') || (c>='A' && c<='Z') || c=='-'){
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            } else {
                return false;
            }
        }

        // 找到第一个小写字母
        int firstIdxOfLower = -1;
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c >= 'a' && c <= 'z') {
                firstIdxOfLower = i;
                break;
            }
        }

        if (firstIdxOfLower >= 0) {
            // 往后寻找大写字母
            for (int i = firstIdxOfLower + 1; i < s.length(); i++) {
                final char c = s.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    return false;
                }
            }
        }

        if (s.matches(".*[A-Z][A-Z][a-z].*")) {
            return false;
        }

        return true;
    }

    /**
     * 得到字符串中指定字符的计数
     *
     * @param s
     * @param c
     * @return
     */
    public static int count(String s, char c) {
        int cnt = 0;

        for (char ci : s.toCharArray()) {
            if (ci == c) {
                cnt++;
            }
        }

        return cnt;
    }

    /**
     * 得到字符串中指定字符的计数
     *
     * @param s
     * @param pattern
     * @return
     */
    public static int count(String s, String pattern) {
        return count(s, Pattern.compile(pattern));
    }


    public static int count(String s, Pattern pattern) {
        final Matcher matcher = pattern.matcher(s);
        int start = 0;
        int count = 0;
        while (true) {
            final boolean b = matcher.find(start);
            if (!b) {
                break;
            }
            count++;
            start = matcher.end();
        }

        return count;
    }

    /**
     * 数组转换成字符串
     *
     * @param list
     * @param delimiter
     * @return
     */
    public static String listToString(ArrayList list, String delimiter) {
        if (list == null) {
            return null;
        } else if (list.size() == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                sb.append(delimiter).append(list.get(i));
            }

            return sb.toString();
        }
    }

    /**
     * 数组转换成字符串
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String collectionToString(Collection collection, String delimiter) {
        if (collection == null) {
            return null;
        } else if (collection.size() == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            final Iterator iterator = collection.iterator();
            sb.append(iterator.next());
            while (iterator.hasNext()) {
                sb.append(delimiter).append(iterator.next());
            }

            return sb.toString();
        }
    }

    /**
     * 数组转换成字符串
     *
     * @param as
     * @param delimiter
     * @return
     */
    public static String arrayToString(Object[] as, String delimiter) {
        if (as == null) {
            return null;
        } else if (as.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(as[0]);
            for (int i = 1; i < as.length; i++) {
                sb.append(delimiter).append(as[i]);
            }

            return sb.toString();
        }
    }

    /**
     * 数组转换成字符串
     *
     * @param as
     * @param delimiter
     * @return
     */
    public static String arrayToString(String[] as, String delimiter) {
        if (as == null) {
            return null;
        } else if (as.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(as[0]);
            for (int i = 1; i < as.length; i++) {
                sb.append(delimiter).append(as[i]);
            }

            return sb.toString();
        }
    }


    /**
     * 数组转换成字符串
     *
     * @param params
     * @param delimiter
     * @return
     */
    public static String arrayToString(String delimiter, Object... params) {
        if (params == null) {
            return null;
        } else if (params.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(params[0]);
            for (int i = 1; i < params.length; i++) {
                sb.append(delimiter).append(params[i]);
            }

            return sb.toString();
        }
    }

    /**
     * 数组转换成字符串
     *
     * @param as
     * @param delimiter
     * @return
     */
    public static String arrayToString(double[] as, String delimiter) {
        if (as == null) {
            return null;
        } else if (as.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(as[0]);
            for (int i = 1; i < as.length; i++) {
                sb.append(delimiter).append(as[i]);
            }

            return sb.toString();
        }
    }


    /**
     * 为了保证插入数据库不超长，根据语种截断字符串。
     */
//    public static String subStringByLanguageForDB(String s, int languageId, DBDataType dbDataType) {
//        return subStringByLanguage(s, languageId, dbDataType.getMaxBytes());
//    }


    /**
     * 为了保证插入数据库不超长，根据语种截断字符串。
     */
//    public static String subStringByLanguage(String s, int languageId, long maxBytes) {
//        if(s == null || "".equals(s)) {
//            return s;
//        }
//
//        int chars = s.length();
//        long maxChars = maxBytes / getDBBytesPerChar(languageId);
//
//        if(chars <= maxChars) {
//            return s;
//        } else {
//            return subString(s, 0, (int) maxChars);
//        }
//    }


    /**
     * 根据语种，得到每个字符在数据库中占用的字节数
     */
//    public static int getDBBytesPerChar(int languageId) {
//        if (languageId == LanguageUtil.IDX_ENGLISH) {
//            return 1;
//        } else {
//            return 3;
//        }
//    }


    /**
     * 词大小写的枚举
     */
    public static enum WordCaseEnum {
        allCaps, lowercase, mixCaps, initial
    }


    /**
     * 得到词的大小写
     *
     * @param s
     * @return
     */
    public static WordCaseEnum getWordCase(String s) {
        if (s.equals(s.toLowerCase())) {
            return WordCaseEnum.lowercase;
        } else if (s.equals(s.toUpperCase())) {
            return WordCaseEnum.allCaps;
        } else {
            final char c = s.charAt(0);
            if (c >= 'A' && c <= 'Z') {
                String substring = s.substring(1);
                if (substring.equals(substring.toLowerCase())) {
                    // 后面全小写
                    return WordCaseEnum.initial;
                } else {
                    // 后面有大写
                    // 但是不可能全大写
                    return WordCaseEnum.mixCaps;
                }
            } else {
                // 第一个字母小写
                // 但是后面不可能全小写
                return WordCaseEnum.mixCaps;
            }
        }
    }

    /**
     * 从后往前得到子串
     *
     * @param s
     * @param idx
     * @return
     */
    public static String substringFromLast(String s, int idx) {
        return s.substring(0, s.length() - idx);
    }

    /**
     * 检查是否匹配
     * false: "abc def" "ab"
     * true: "abc def" "abc"
     *
     * @param str
     * @param subStr
     * @return
     */
    public static boolean isMatchByWord(String str, String subStr) {
        if (str == null || subStr == null) {
            return false;
        }

        if (str.length() < subStr.length()) {
            // 母串比子串短
            return false;
        }

        int start = str.indexOf(subStr);
        if (start < 0) {
            //  完全不匹配
            return false;
        } else if (start == 0) {
            // 母串以字串开始
            if (str.length() == subStr.length()) {
                // 完全相同
                return true;
            } else {
                // 如果下一个字符是分隔符，那么匹配
                return isSplit(str.charAt(subStr.length()));
            }
        } else if (start + subStr.length() == str.length()) {
            // 母串以字串结尾
            // 如果上一个字符是分隔符，那么匹配
            return isSplit(str.charAt(start - 1));
        } else {
            // 子串在母串中间
            return isSplit(str.charAt(start - 1))
                    && isSplit(str.charAt(subStr.length()));
        }
    }

    public static boolean isSplit(char c) {
        return c == ' '
                || c == '\t'
                || c == '-'
                || c == '_'
                || c == '.'
                || c == '·'
                ;
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s);
    }


    /**
     * 判断字符串是否为空
     */
    public static String ifEmpty(String s, String valueIfEmpty) {
        if (isEmpty(s)) {
            return valueIfEmpty;
        } else {
            return s;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean notEmptyOrBlank(String s) {
        return s != null && !"".equals(s) && !"".equals(s.trim());
    }

    /**
     * 判断字符串长度是否大于（不含等于）指定长度
     * null的长度为0
     *
     * @param s
     * @return
     */
    public static boolean longerThan(String s, int threshold) {
        return s != null && !"".equals(s) && s.length() > threshold;
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isTrimEmpty(String s) {
        return s == null || "".equals(s) || "".equals(s.trim());
    }

    /**
     * @param s
     * @return
     */
    public static String subStringByLength(String s, int start, int length) {
        if (s == null) {
            return null;
        } else if ("".equals(s)) {
            return "";
        } else {
            int end = Math.min(s.length(), start + length);
            return s.substring(start, end);
        }
    }

    /**
     * @param s
     * @return
     */
    public static String subString(String s, int start, int end) {
        if (s == null) {
            return null;
        } else if ("".equals(s)) {
            return "";
        } else {
            if (start < 0) {
                start = 0;
            }

            if (end < 0) {
                return null;
            }

            if (start >= s.length()) {
                return null;
            }

            if (end > s.length()) {
                end = s.length();
            }

            if (start > end) {
                start = end;
            }

            return s.substring(start, end);
        }
    }

    /**
     * @param s
     * @return
     */
    public static String subString(String s, int start) {
        if (s == null) {
            return s;
        }
        return subString(s, start, s.length());
    }

    /**
     * 判断字符串是否相同
     *
     * @return
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equalsIgnoreCase(s2);
        }
    }

    /**
     * 判断字符串是否相同
     *
     * @return
     */
    public static boolean notEquals(String s1, String s2) {
        return !equals(s1, s2);
    }

    /**
     * 判断字符串是否相同
     *
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    public static String trim(String s) {
        if (s == null) {
            return null;
        } else if ("".equals(s)) {
            return s;
        } else {
            return s.trim();
        }
    }


    public static final String[] strBlanks = new String[]{
            "",
            " ",
            "  ",
            "   ",
            "    ",
            "     ",
            "      ",
            "       ",
            "        ",
            "         ",
            "          "
    };

    public static String getBlank(int n) {
        if (n < strBlanks.length) {
            return strBlanks[n];
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < n; i++) {
                sb.append(' ');
            }
            return sb.toString();
        }
    }


    public static String duplicate(char s, long n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }


    public static String duplicate(String s, int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }


    public static boolean isPureEnglish(String name) {
        if (name == null || "".equals(name)) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'A')) {
                // ok
            } else {
                return false;
            }
        }

        return true;
    }


    public static String upperInitial(String s) {
        if (s == null) {
            return null;
        }

        if ("".equals(s)) {
            return s;
        }

        char firstChar = s.charAt(0);

        if (firstChar >= 'a' && firstChar <= 'z') {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    public static String lowerInitial(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }

        char firstChar = s.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            firstChar += 32;
            return firstChar + s.substring(1);
        } else {
            return s;
        }
    }


    /**
     * 检查是否是英文字母
     *
     * @param c
     * @return
     */
    public static boolean isEnglishLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }


    /**
     * 检查是否是数字
     *
     * @param c
     * @return
     */
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    /**
     * 返回字符类型
     *
     * @param c
     * @return
     */
    public static CharType getCharType(char c) {
        if (isEnglishLetter(c)) {
            return CharType.EnglishLetter;
        } else if (isDigit(c)) {
            return CharType.digit;
        } else {
            return CharType.other;
        }
    }


    /**
     * 两个字符是否可以连接
     *
     * @param leftCharType
     * @param rightCharType
     * @return
     */
    public static boolean concatable(CharType leftCharType, CharType rightCharType) {
        // 字母和字母连接
        // 数字和数字连接
        return
                (leftCharType == CharType.EnglishLetter && rightCharType == CharType.EnglishLetter)
                        || (leftCharType == CharType.digit && rightCharType == CharType.digit);
    }


//    public static String truncateByLanguageId(String s, int languageId, int maxLengthInDB) {
//        if (s == null) {
//            // ok
//        } else if (languageId == LanguageUtil.IDX_ENGLISH) {
//            if (s.length() > maxLengthInDB) {
//                s = s.substring(0, maxLengthInDB);
//            }
//        } else {
//            if (s.length() > maxLengthInDB / 3) {
//                s = s.substring(0, maxLengthInDB / 3);
//            }
//        }
//        return s;
//    }


    /**
     * 将inputStream转换为String
     *
     * @param is
     * @return
     */
    public static String inputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 将inputStream转换为String
     *
     * @param is
     * @return
     */
    public static String inputStreamToString(String contentEncoding, InputStream is) throws IOException {
        BufferedReader reader;
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
        } else {
            reader = new BufferedReader(new InputStreamReader(is, contentEncoding));
        }

        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 将inputHtml转化为string
     * 解决乱码问题
     *
     * @param inputHtml
     * @return
     * @throws IOException
     */
    public static String StreamToString(InputStream inputHtml) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = inputHtml.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    public static String inputStreamToString(InputStream is, String e1, String e2, String e3) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, e1));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes(e2), e3);
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    /**
     * 获得字符串的长度
     *
     * @param str
     * @return
     */
    public static int length(String str) {
        if (str == null) {
            return 0;
        } else {
            return str.length();
        }
    }


    /**
     * 统计乱码字符数
     */
    public static int countWrongChar(String text) {
        if (isEmpty(text)) {
            return 0;
        } else {
            int total = 0;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '�') {
                    total++;
                }
            }
            return total;
        }
    }


    /**
     * 统计乱码字符比率
     */
    public static double ratioWrongChar(String text) {
        if (isEmpty(text)) {
            return 0;
        } else {
            int total = text.length();
            int wrong = countWrongChar(text);
            return wrong * 1.0 / total;
        }
    }

    /**
     * 如果str为null则返回“”
     */
    public static String replaceNullWithBlank(String str) {
        if (notEmpty(str)) {
            return str;
        } else {
            return "";
        }
    }


    /**
     * 如果str为“”则返回null
     */
    public static String replaceBlankWithNull(String str) {
        if ("".equals(str)) {
            return null;
        } else {
            return str;
        }
    }


    /**
     * return true if and only if name contains subStr
     */
    public static boolean containsWord(String str, String subStr, String splitter) {
        return indexOf(str, subStr, splitter) >= 0;
    }


    /**
     * return the index within name of the first occurrence of subStr
     */
    public static int indexOf(String str, String subStr, String splitter) {
        if (isEmpty(str) || isEmpty(subStr)) {
            return -1;
        }
        int lastStartIdx = 0;
        while (true) {
            int startIdx = str.indexOf(subStr, lastStartIdx);
            if (startIdx < 0) {
                return -1;
            }

            // check before-name
            boolean isBeforeOk;
            if (startIdx == 0) {
                // ok
                isBeforeOk = true;
            } else {
                // there must be a splitter before the subStr
                if (startIdx < splitter.length()) {
                    isBeforeOk = false;
                } else {
                    if (!str.substring(startIdx - splitter.length(), startIdx).equals(splitter)) {
                        isBeforeOk = false;
                    } else {
                        // ok
                        isBeforeOk = true;
                    }
                }
            }

            if (isBeforeOk) {
                // check after-name
                int endIdx = startIdx + subStr.length();
                if (endIdx == str.length()) {
                    // ok
                    return startIdx;
                } else {
                    // there must be a splitter after the subStr
                    if (endIdx + splitter.length() > str.length()) {
                        // not ok
                    } else {
                        if (!str.substring(endIdx, endIdx + splitter.length()).equals(splitter)) {
                            // not ok
                        } else {
                            return startIdx;
                        }
                    }
                }
            }

            lastStartIdx = startIdx;
        }
    }

    /**
     * 将null替换为--
     *
     * @param str
     */
    public static String replaceNullWithHorizontal(String str) {
        if (isEmpty(str)) {
            return "--";
        } else{
            return str;
        }
    }


    public static String mysqlEscapeStr(String str) {
        if (isEmpty(str)) str = "";
        return addSlashes(str);
    }

    public static String addSlashes(String str) {
        if (str == null) return null;
        str = str.replaceAll("\\\\", "\\\\\\\\");
        str = str.replaceAll("\\n", "\\\\n");
        str = str.replaceAll("\\r", "\\\\r");
        str = str.replaceAll("\\00", "\\\\0");
        str = str.replaceAll("'", "\\\\'");
        return str;
    }
}
