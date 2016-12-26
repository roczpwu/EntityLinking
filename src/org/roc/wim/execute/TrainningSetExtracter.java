package org.roc.wim.execute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.roc.wim.entityLinking.BeanFactory;
import org.roc.wim.entityLinking.el.doc.Doc;
import org.roc.wim.entityLinking.el.doc.DocBL;
import org.roc.wim.entityLinking.el.doc.DocCache;
import org.roc.wim.entityLinking.el.mention.Mention;
import org.roc.wim.entityLinking.el.mention.MentionBL;
import org.roc.wim.entityLinking.el.mention.MentionCache;
import org.roc.wim.entityLinking.el.trainningSet.TrainningSet;
import org.roc.wim.entityLinking.el.trainningSet.TrainningSetBL;
import org.roc.wim.entityLinking.expriments.Features;
import org.roc.wim.entityLinking.wiki.doctionary.Candidate;
import org.roc.wim.entityLinking.wiki.doctionary.CandidateCache;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstCache;

import java.util.Collections;
import java.util.List;

/**
 * User: rocwu
 * Date: 2016/12/21
 * Time: 14:51
 * Desc: 训练数据提取
 */
public class TrainningSetExtracter {

    private static Logger logger = LogManager.getLogger(TrainningSetExtracter.class.getName());

    public static void main(String[] args) {
        MentionBL mentionBL = (MentionBL) BeanFactory.getBean("mentionBL");
        MentionCache mentionCache = (MentionCache) BeanFactory.getBean("mentionCache");
        CandidateCache candidateCache = (CandidateCache) BeanFactory.getBean("candidateCache");
        DocCache docCache = (DocCache) BeanFactory.getBean("docCache");
        PageAbstCache pageAbstCache = (PageAbstCache) BeanFactory.getBean("pageAbstCache");
        Features features = (Features) BeanFactory.getBean("features");
        features.init(new String[]{"PriorityProbability", "ContextSimilarity", "EditDistance",
            "MentionContainTitle", "TitleContainsMention", "EntityDice", "EntityLink"});
        TrainningSetBL trainningSetBL = (TrainningSetBL) BeanFactory.getBean("trainningSetBL");
        List list = mentionBL.getListByCondition(Mention.NeType+" != 'MISC' and "+Mention.EntityId+" > '0'");
        for (int i = 0; i < list.size(); i++) {
            Mention mention = (Mention) list.get(i);
            String mentionName = mention.getName();
            String entityTitle = mention.getWikiTitle();
            List<Candidate> candidateList = candidateCache.get(mentionName);
            Doc doc = docCache.get(mention.getDocId());
            String mentionContext = doc.getContent();
            //上下文中的其他mention，取5个
            int contextMentionCount = 5;
            List contextMentionList = mentionCache.get(doc.getId());
            int index = 0;
            for (int j = 0; j < contextMentionList.size(); j++) {
                Mention _mention = (Mention) contextMentionList.get(j);
                if (_mention.getId() == mention.getId()) {
                    index = j;
                    break;
                }
            }
            if (contextMentionList.size()<contextMentionCount+1)
                contextMentionCount = contextMentionList.size()-1;
            String[] contextMentions = new String[contextMentionCount];
            int left_index = index-1, right_index = index+1, current_index = 0;
            while (true) {
                if (current_index>=contextMentionCount) break;
                if (left_index < 0) contextMentions[current_index++] = ((Mention)contextMentionList.get(right_index++)).getName();
                else if (right_index >= contextMentionList.size()) contextMentions[current_index++] = ((Mention)contextMentionList.get(left_index--)).getName();
                else {
                    int left_dis = ((Mention)contextMentionList.get(index)).getSeqInDoc()-((Mention)contextMentionList.get(left_index)).getSeqInDoc();
                    int right_dis = ((Mention)contextMentionList.get(right_index)).getSeqInDoc()-((Mention)contextMentionList.get(index)).getSeqInDoc();
                    if (left_dis < right_dis) contextMentions[current_index++] = ((Mention)contextMentionList.get(left_index--)).getName();
                    else contextMentions[current_index++] = ((Mention)contextMentionList.get(right_index++)).getName();
                }
            }
            for (Candidate candidate : candidateList) {
                String candidateTitle = candidate.getWikiTitle();
                int candidateId = candidate.getEntityId();
                String candidateAbst = pageAbstCache.get(candidateId);
                float[] feature_values = features.generateFeatureVector(mentionContext, candidateAbst, mentionName, candidateTitle, contextMentions);
                TrainningSet trainningSet = new TrainningSet();
                trainningSet.setMentionId(mention.getId());
                trainningSet.setName(mention.getName());
                trainningSet.setEntityId(candidateId);
                trainningSet.setTitle(candidateTitle);
                trainningSet.setPriorityProbability(feature_values[0]);
                trainningSet.setContextSimilarity(feature_values[1]);
                trainningSet.setEditDistance(feature_values[2]);
                trainningSet.setMentionContainTitle(feature_values[3]);
                trainningSet.setTitleContainsMention(feature_values[4]);
                trainningSet.setEntityDice(feature_values[5]);
                trainningSet.setEntityLink(feature_values[6]);
                if (entityTitle.equals(candidate.getWikiTitle())) trainningSet.setIsCorrect(1);  // 正例
                else trainningSet.setIsCorrect(0);                                               // 反例
                //trainningSetBL.save(trainningSet);
            }
            logger.info((i+1)+" mentions completed, total "+ list.size());
        }
    }
}
