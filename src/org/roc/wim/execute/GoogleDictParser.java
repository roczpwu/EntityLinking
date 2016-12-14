package org.roc.wim.execute;

import com.roc.core.Utils.StringUtil;
import com.roc.core.base.BaseDTO;
import org.roc.wim.entityLinking.BeanFactory;
import org.roc.wim.entityLinking.el.doc.Doc;
import org.roc.wim.entityLinking.el.doc.DocBL;
import org.roc.wim.entityLinking.wiki.doctionary.Dictionary;
import org.roc.wim.entityLinking.wiki.doctionary.DictionaryBL;

import java.io.*;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/23
 * Time: 14:25
 * Desc: google词典候选实体解析
 *       写入wiki.dictionary
 */
public class GoogleDictParser {

    private static Set<Character> characterSet;

    public static void main(String[] args) {

        getCharacterSet();

        DictionaryBL dictionaryBL = (DictionaryBL) BeanFactory.getBean("dictionaryBL");

        File file = new File("E:\\Entity Linking FTP\\dictionary");
        BufferedReader reader = null;
        int insertRowNum = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            List<BaseDTO> saveList = new ArrayList<>();
            while (true) {
                String strLine = reader.readLine();
                if (strLine == null) {
                    dictionaryBL.save(saveList);
                    break;
                }
                int tabPos = strLine.indexOf("\t");
                if (tabPos <= 0) continue;  // 过滤name为空串的记录
                String name = strLine.substring(0, tabPos).trim();
                if(name.endsWith(" (disambiguation)")
                        || name.startsWith("List of ")) {
                    continue;
                }
                if (StringUtil.isEmpty(name)) continue;
                if (!isEnglish(name)) continue;
                int spacePos = strLine.indexOf(" ", tabPos);
                float cprob = Float.parseFloat(strLine.substring(tabPos+1, spacePos));
                Dictionary dictionary = new Dictionary();
                dictionary.setName(name);
                dictionary.setNameUpperCase(name.toUpperCase());
                dictionary.setProbOfNameToEntity(cprob);
                String rightStr = strLine.substring(spacePos+1);
                int space2Pos = rightStr.indexOf(" ");
                if (space2Pos < 0) dictionary.setWikiTitle(rightStr);
                else dictionary.setWikiTitle(rightStr.substring(0, space2Pos));
                if (dictionary.getName().length()>255 || dictionary.getWikiTitle().length()>255) continue;
                saveList.add(dictionary);
                //dictionaryBL.save(dictionary);
                if (saveList.size()>=10000) {
                    insertRowNum += dictionaryBL.save(saveList);
                    System.out.println("insert rows : " + insertRowNum);
                    saveList.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void getCharacterSet() {
        characterSet = new HashSet<>();
        DocBL docBL = (DocBL) BeanFactory.getBean("docBL");
        List<Doc> docList = docBL.getList();
        for (Doc doc : docList) {
            String content = doc.getContent();
            for (char ch : content.toCharArray())
                characterSet.add(ch);
        }
        System.out.println("done");
    }

    private static boolean isEnglish(String str) {
        for (char ch : str.toCharArray()) {
            if (!characterSet.contains(ch))
                return false;
        }
        return true;
    }
}
