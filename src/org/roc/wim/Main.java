package org.roc.wim;

import org.roc.wim.entityLinking.BeanFactory;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityDiceSimilarityModel;
import org.roc.wim.entityLinking.caculateModel.entity_entity_similarity.EntityLinkSimilarityCache;
import org.roc.wim.entityLinking.el.entity.EntityDAO;
import org.roc.wim.entityLinking.expriments.DataSet;
import org.roc.wim.entityLinking.expriments.Features;
import org.roc.wim.entityLinking.expriments.Trainer;
import org.roc.wim.entityLinking.expriments.expriment2.ConfidenceBasedTrainer;
import org.roc.wim.entityLinking.utils.StanfordUtil;
import org.roc.wim.entityLinking.wiki.doctionary.Candidate;
import org.roc.wim.entityLinking.wiki.doctionary.CandidateCache;
import org.roc.wim.entityLinking.wiki.doctionary.DictionaryBL;
import weka.classifiers.Classifier;

import java.util.List;

/**
 * User: rocwu
 * Date: 2016/11/16
 * Time: 14:08
 * Desc: main
 */
public class Main {
    public static void main(String[] args) throws Exception {

        ConfidenceBasedTrainer trainer = (ConfidenceBasedTrainer) BeanFactory.getBean("confidenceBasedTrainer");
        trainer.initTrainSet(2000, true);
        System.out.println(trainer);
        Classifier classifier = trainer.train();
        System.out.println(classifier);
        trainer.validate();

//        CandidateCache cache = (CandidateCache) BeanFactory.getBean("candidateCache");
//        List<Candidate> list = cache.get("European Union");
//        System.out.println(list);

//        EntityLinkSimilarityCache entityLinkSimilarityCache = (EntityLinkSimilarityCache) BeanFactory.getBean("entityLinkSimilarityCache");
//        System.out.println(entityLinkSimilarityCache.get(9878520, 486042));//Chang_Yu-sheng   A-mei
//        System.out.println(entityLinkSimilarityCache.get(11265359, 543675));//Vincent_Fang    Jay_Chou
//        System.out.println(entityLinkSimilarityCache.get(11265359, 2017814));//Vincent_Fang    Xi_Jinping
//        System.out.println(entityLinkSimilarityCache.get(95871, 2017814));//Jiang_Zemin    Xi_Jinping
//        System.out.println(entityLinkSimilarityCache.get(543675, 2017814));//Jay_Chou    Xi_Jinping
//        System.out.println(entityLinkSimilarityCache.get(3708, 9974));//Brussels    European_Commission
//        System.out.println(entityLinkSimilarityCache.get(3708, 11867));//Brussels    Germany
//
//        System.out.println();
//
//        EntityDiceSimilarityModel entityDiceSimilarityModel = (EntityDiceSimilarityModel) BeanFactory.getBean("entityDiceSimilarityModel");
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(9878520, 486042));//Chang_Yu-sheng   A-mei
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(11265359, 543675));//Vincent_Fang    Jay_Chou
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(11265359, 2017814));//Vincent_Fang    Xi_Jinping
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(95871, 2017814));//Jiang_Zemin    Xi_Jinping
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(543675, 2017814));//Jay_Chou    Xi_Jinping
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(3708, 9974));//Brussels    European_Commission
//        System.out.println(entityDiceSimilarityModel.calcSimilarity(3708, 11867));//Brussels    Germany

//        DictionaryBL dictionaryBL = (DictionaryBL) BeanFactory.getBean("dictionaryBL");
//        List list = dictionaryBL.getCandidateTitles("Bill Gates", true);
//        System.out.println(list);
    }
}
