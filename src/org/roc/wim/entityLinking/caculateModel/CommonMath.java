package org.roc.wim.entityLinking.caculateModel;

import com.roc.core.Utils.StringUtil;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 14:25
 * Desc: 常用计算模型
 */
public class CommonMath {

    public static int calcEditDistance(String str1, String str2, boolean caseSensitive) {
        int len1 = StringUtil.isEmpty(str1) ? 0 : str1.length();
        int len2 = StringUtil.isEmpty(str2) ? 0 : str2.length();
        if (len1==0||len2==0) return Math.max(len1, len2);
        if (!caseSensitive) {
            str1 = str1.toUpperCase();
            str2 = str2.toUpperCase();
        }
        int[][] dp = new int[len1+1][len2+1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i==0||j==0) dp[i][j] = Math.max(i, j);
                else {
                    dp[i][j] = Integer.MAX_VALUE;
                    if (str1.charAt(i-1) == str2.charAt(j-1))
                        dp[i][j] = Math.min(dp[i-1][j-1], dp[i][j]);
                    if (i>=1) dp[i][j] = Math.min(dp[i-1][j]+1, dp[i][j]);
                    if (j>=1) dp[i][j] = Math.min(dp[i][j-1]+1, dp[i][j]);
                }
            }
        }
        return dp[len1][len2];
    }
}
