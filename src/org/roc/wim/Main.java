package org.roc.wim;

import org.roc.wim.entityLinking.BeanFactory;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityDiceSimilarityModel;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityLinkSimilarityCache;
import org.roc.wim.entityLinking.el.entity.EntityDAO;
import org.roc.wim.entityLinking.wiki.doctionary.DictionaryBL;

import java.util.List;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 14:08
 * Desc: main
 */
public class Main {
    public static void main(String[] args) throws Exception {

        EntityLinkSimilarityCache entityLinkSimilarityCache = (EntityLinkSimilarityCache) BeanFactory.getBean("entityLinkSimilarityCache");
        System.out.println(entityLinkSimilarityCache.get(9878520, 486042));//Chang_Yu-sheng   A-mei
        System.out.println(entityLinkSimilarityCache.get(11265359, 543675));//Vincent_Fang    Jay_Chou
        System.out.println(entityLinkSimilarityCache.get(11265359, 2017814));//Vincent_Fang    Xi_Jinping
        System.out.println(entityLinkSimilarityCache.get(95871, 2017814));//Jiang_Zemin    Xi_Jinping
        System.out.println(entityLinkSimilarityCache.get(543675, 2017814));//Jay_Chou    Xi_Jinping

        EntityDiceSimilarityModel entityDiceSimilarityModel = (EntityDiceSimilarityModel) BeanFactory.getBean("entityDiceSimilarityModel");
        System.out.println(entityDiceSimilarityModel.calcSimilarity(9878520, 486042));//Chang_Yu-sheng   A-mei
        System.out.println(entityDiceSimilarityModel.calcSimilarity(11265359, 543675));//Vincent_Fang    Jay_Chou
        System.out.println(entityDiceSimilarityModel.calcSimilarity(11265359, 2017814));//Vincent_Fang    Xi_Jinping
        System.out.println(entityDiceSimilarityModel.calcSimilarity(95871, 2017814));//Jiang_Zemin    Xi_Jinping
        System.out.println(entityDiceSimilarityModel.calcSimilarity(543675, 2017814));//Jay_Chou    Xi_Jinping

        DictionaryBL dictionaryBL = (DictionaryBL) BeanFactory.getBean("dictionaryBL");
        List list = dictionaryBL.getCandidateTitles("BILL GATES");
        System.out.println(list);
    }
}
