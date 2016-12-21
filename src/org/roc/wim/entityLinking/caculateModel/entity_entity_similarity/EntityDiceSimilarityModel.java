package org.roc.wim.entityLinking.caculateModel.entity_entity_similarity;

import com.roc.core.Utils.CountTable;
import com.roc.core.Utils.StringUtil;
import org.roc.wim.entityLinking.el.stopWords.StopWords;
import org.roc.wim.entityLinking.utils.StanfordUtil;
import org.roc.wim.entityLinking.wiki.doctionary.Candidate;
import org.roc.wim.entityLinking.wiki.doctionary.CandidateCache;
import org.roc.wim.entityLinking.wiki.page.Page;
import org.roc.wim.entityLinking.wiki.page.PageBL;
import org.roc.wim.entityLinking.wiki.page.Title2IdCache;
import org.roc.wim.entityLinking.wiki.pageAbst.PageAbstCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: rocwu
 * Date: 2016/12/12
 * Time: 16:25
 * Desc: 实体摘要之间Dice相似度计算模型
 *                   2 * |abst_token(ei)∩abst_token(ej)|
 *      r(ei,ej) = ----------------------------------------
 *                    |abst_token(ei)|+|abst_token(ej)|
 */
@Service
public class EntityDiceSimilarityModel {
    @Autowired
    private PageAbstCache pageAbstCache;
    @Autowired
    private CandidateCache candidateCache;
    @Autowired
    private Title2IdCache title2IdCache;

    public float calcSimilarity(int entityId1, int entityId2) {
        String abst1 = pageAbstCache.get(entityId1);
        String abst2 = pageAbstCache.get(entityId2);
        if (StringUtil.isEmpty(abst1) || StringUtil.isEmpty(abst2))
            return 0.0f;
        String[] abst1TokenList = StanfordUtil.Tokenize(abst1);
        String[] abst2TokenList = StanfordUtil.Tokenize(abst2);
        CountTable abst1TokenCount = new CountTable();
        int unionCount = 0;
        int abst1Count = 0, abst2Count = 0;
        for (String token : abst1TokenList) {
            if (!StopWords.isStopword(token)) {
                abst1Count++;
                abst1TokenCount.add(token);
            }
        }
        for (String token : abst2TokenList) {
            if (!StopWords.isStopword(token))
                abst2Count++;
            if (abst1TokenCount.getCount(token)>0) {
                unionCount++;
                abst1TokenCount.delete(token);
            }
        }
        return 2.0f * unionCount / (abst1Count + abst2Count);
    }

    /**
     * 上下文环境中mention对应实体的相似度
     * @param entityId
     * @param mentions
     * @return
     */
    public float calcSimilarity(int entityId, String[] mentions) {
        if (mentions == null || mentions.length == 0) return  0.0f;
        float result = 0.0f;
        for (int i = 0; i < mentions.length; i++) {
            List<Candidate> titles = candidateCache.get(mentions[i]);
            if (titles.size()>0) result += calcSimilarity(entityId, titles.get(0).getEntityId());
        }
        return result / mentions.length;
    }

    /**
     * 上下文环境中mention对应实体的相似度
     * @param title
     * @param mentions
     * @return
     */
    public float calcSimilarity(String title, String[] mentions) {
        int pageId = title2IdCache.get(title);
        return calcSimilarity(pageId, mentions);
    }
}
