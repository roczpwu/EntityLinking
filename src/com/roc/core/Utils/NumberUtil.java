package com.roc.core.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 这个类用来将文本数字，如"Four"，转成"4"
 * 用个hashMap来做这个事情吧
 * 做单例？
 *
 * @author 彭程
 */
public class NumberUtil {
    // 字符串到数字的map
    private HashMap<String, String> map = new HashMap<String, String>();
    public static Character[] CHINESE_DIGITS = new Character[]{
            '零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十'
    };
    public static Character[] CHINESE_BASE = new Character[]{
            '一', '十', '百', '千', '万'
    };

    // 单例
    private static NumberUtil instance = null;

    // 私有构造函数
    private NumberUtil() {
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        map.put("four", "4");
        map.put("five", "5");
        map.put("six", "6");
        map.put("seven", "7");
        map.put("eight", "8");
        map.put("nine", "9");
        map.put("eleven", "11");
        map.put("twelve", "12");
        map.put("thirteen", "13");
        map.put("fourteen", "14");
        map.put("fiveteen", "15");
        map.put("sixteen", "16");
        map.put("seventeen", "17");
        map.put("eighteen", "18");
        map.put("nineteen", "19");
        map.put("一", "1");
        map.put("二", "2");
        map.put("三", "3");
        map.put("四", "4");
        map.put("五", "5");
        map.put("六", "6");
        map.put("七", "7");
        map.put("八", "8");
        map.put("九", "9");
        map.put("十", "10");
    }

    /**
     * 单例函数
     *
     * @return 一个NumberUtil的实例
     */
    public static synchronized NumberUtil getInstance() {
        if (instance == null) {
            instance = new NumberUtil();
        }
        return instance;
    }

    /**
     * 获取字符串对应的数字
     *
     * @param strNum
     * @return
     */
    public synchronized String StrToNum(String strNum) {
        // 先讲单词转化为数字字符串
        String tempStrNumString = "";
        if (map.containsKey(strNum.toLowerCase())) {
            tempStrNumString = map.get(strNum.toLowerCase());
        }
        // 判断得到的字符串能否转化为整型类型
        tempStrNumString = isInteger(tempStrNumString);
        return tempStrNumString;
    }

    /**
     * 判断字符串可否返回整型类型，不行则返回默认字符串
     *
     * @param str
     * @return
     */
    public String isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
//			return SubTask.DEFAULTVALUE;
        }
        return str;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isDecimal(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }

        boolean hasDotOccurred = false;
        final int length = str.length();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                // ok
            } else if (c == '.') {
                if (!hasDotOccurred) {
                    hasDotOccurred = true;
                } else {
                    // 再次出现点号，不是数字
                    return false;
                }
                // 不能以点结尾
                if (i == length - 1) {
                    return false;
                }
            } else {
                return false;
            }
        }

        // 如果以零开头
        if (str.charAt(0) == '0') {
            if (length == 1) {
                // ok
            } else if (str.charAt(1) == '.') {
                // ok
            } else {
                return false;
            }
        }

        return true;
    }


    public static boolean approximatelyEqual(double d0, double d1) {
        return Math.abs(d0 - d1) < 0.0000000001;
    }


    public static String format(double d, Integer numOfFractional) {
        return format(d, numOfFractional, numOfFractional);
    }

    /**
     * 格式化输出 浮点数
     *
     * @param d   双精度浮点数
     * @param maxNumOfFractional 小数点后-最大保留位数
     * @param minNumOfFractional 小数点后-最小保留位数(默认为 2 ,不足补0)
     * @return
     */
    public static String format(double d, Integer maxNumOfFractional, Integer minNumOfFractional) {
        if(Double.isNaN(d)) {
            return "NaN";
        }
        Integer _min = (null == minNumOfFractional || minNumOfFractional < 0) ? 2 : minNumOfFractional;
        String pattern = "0";
        DecimalFormat formatter = new DecimalFormat(pattern);
        if (null != _min) {
            formatter.setMinimumFractionDigits(_min);
        }
        if (null != maxNumOfFractional) {
            formatter.setMaximumFractionDigits(maxNumOfFractional);
        }
        return formatter.format(d);
    }


    private static void test_isDecimal() {
        class TestCase {
            public String str;
            public boolean isDecimal;

            TestCase(String str, boolean decimal) {
                this.str = str;
                isDecimal = decimal;
            }
        }

        List<TestCase> list = new ArrayList<TestCase>();
        list.add(new TestCase("0.01", true));
        list.add(new TestCase("123", true));
        list.add(new TestCase("123.00", true));
        list.add(new TestCase("a", false));
        list.add(new TestCase("1a3", false));
        list.add(new TestCase(".01", true));
        list.add(new TestCase("0.", false));
        list.add(new TestCase("0", true));
        list.add(new TestCase("0.1.1", false));
        list.add(new TestCase(".", false));
        list.add(new TestCase("1.", false));

        System.out.println("str\t\t\twzp.newsML.MailSample\tanswer\tresult");
        System.out.println("---------------------------------------");
        for (TestCase testCase : list) {
            final boolean test = isDecimal(testCase.str);
            final boolean answer = testCase.isDecimal;
            System.out.println(
                    StringUtil.rightFill(testCase.str, 8, ' ')
                            + "\t" + test
                            + "\t" + answer
                            + "\t" + (answer == test ? "OK" : "Failed")
            );
        }
    }

    public static boolean isEquals(String v0, String v1) {
        if (v0 == null) {
            if (v1 == null) {
                return true;
            } else {
                return false;
            }
        } else {
            // v0 != null
            if (v1 == null) {
                return false;
            }
        }

        return trim(v0).equals(trim(v1));
    }


    /**
     * 去除无用的空格点和零
     */
    public static String trim(String str) {
        if (str == null) {
            return null;
        }

        String temp = str.trim();
        if ("".equals(temp)) {
            return "";
        }

        int idxOfDot = temp.indexOf('.');

        if (idxOfDot < 0) {
            // 没有点
            // 去掉前面的零
            return temp.replaceFirst("^0+", "");
        } else {
            // 有点
            // 开头的多个零合并
            temp = temp.replaceFirst("^0+", "0");
            // 结尾的零去掉
            temp = temp.replaceFirst("0+$", "");
            // 最后的点去掉
            int length = temp.length();
            if (temp.charAt(length - 1) == '.') {
                temp = temp.substring(0, length - 1);
            }

            return temp;
        }
    }


    /**
     * 整数转为中文
     */
    public static String intToChinese(int integer) throws Exception {
        if (integer > 100000 || integer < 0) {
            throw new Exception("不能处理的数字：" + integer);
        }

        StringBuilder sb = new StringBuilder();
        List<Integer> digitList = intToDigitList(integer);
        for (int base = digitList.size() - 1; base >= 0; base--) {
            int digit = digitList.get(base);
            char ChineseDigit = CHINESE_DIGITS[digit];
            sb.append(ChineseDigit);
            if (base > 0) {
                char ChineseBase = CHINESE_BASE[base];
                sb.append(ChineseBase);
            }
        }

        String s = sb.toString();
        if (s.startsWith("一十")) {
            s = s.substring(1);
        }
        s = s.replaceFirst("百零十零", "百");
        s = s.replaceFirst("百零十", "百零");
        return s;
    }

    /**
     * 整数转换为一个个的数字，低位在数组结果的前面
     */
    public static List<Integer> intToDigitList(int i) {
        List<Integer> digitList = new ArrayList<Integer>();
        int base = 1;
        while (true) {
            int digit = (i / base) % 10;
            digitList.add(digit);

            if (i / (base * 10) == 0) {
                break;
            }
            base *= 10;
        }

        return digitList;
    }

    /**
     * 格式化数字
     */
    public static String format(int number, int length) {
        return format(number, length, '0');
    }


    public static String format(int number, int length, char filler) {
        final String s_number = String.valueOf(number);
        StringBuffer sb = new StringBuffer();
        for (int i = s_number.length(); i < length; i++) {
            sb.append(filler);
        }
        sb.append(s_number);

        return sb.toString();
    }


    public static void main(String args[]) throws Exception {
        class TestCase {
            int integer;
            String expectedResult;

            TestCase(int integer, String expectedResult) {
                this.integer = integer;
                this.expectedResult = expectedResult;
            }

            boolean test() throws Exception {
                String testResult = intToChinese(integer);
                boolean pass = StringUtil.equals(testResult, expectedResult);
                String contentToBePrinted = (pass ? "^_^   " : "ERROR ") + integer + "\t" + testResult;
                if (pass) {
                    System.out.println(contentToBePrinted);
                } else {
                    System.err.println(contentToBePrinted);
                }
                return pass;
            }
        }

        new TestCase(1, "一").test();
        new TestCase(9, "九").test();
        new TestCase(12, "十二").test();
        new TestCase(100, "一百").test();
        new TestCase(101, "一百零一").test();
        new TestCase(123, "一百二十三").test();
    }

    /**
     * 生成UUID
     * @return
     */
    public static String generateUUID() {
        UUID uuid  =  UUID.randomUUID();
        return UUID.randomUUID().toString();
    }
}
