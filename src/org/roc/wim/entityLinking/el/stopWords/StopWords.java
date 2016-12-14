package org.roc.wim.entityLinking.el.stopWords;

import com.roc.core.Utils.FileUtil;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 20:30
 * Desc: 停用词表
 */
public class StopWords {

    private static Set<String> set;

    static {
        String basePath = StopWords.class.getResource("/").getFile();
        set = FileUtil.readLinesIntoHashSet(basePath+"org/roc/wim/entityLinking/el/stopWords/stop word.txt");
    }

    public static boolean isStopword(String word) {
        return set.contains(word);
    }
}
