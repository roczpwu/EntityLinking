package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import com.roc.core.Utils.StringUtil;
import org.roc.wim.entityLinking.wiki.doctionary.Dictionary;
import org.roc.wim.entityLinking.wiki.doctionary.DictionaryBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: rocwu
 * Date: 2016/12/14
 * Time: 15:50
 * Desc: 计算给定指称对应候选实体的先验概率
 */
@Service
public class PriorityProbabilityModel {

    @Autowired
    private DictionaryBL dictionaryBL;

    public float calcSimilarity(String mention, String title) {
        Dictionary dictionary = (Dictionary) dictionaryBL.getByCondition(Dictionary.Name+" = '"+StringUtil.mysqlEscapeStr(mention)+"' and "+Dictionary.WikiTitle+" = '"+ StringUtil.mysqlEscapeStr(title)+"'");
        if (dictionary == null) return 0.0f;
        return dictionary.getProbOfNameToEntity();
    }
}
