package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import com.roc.core.Utils.StringUtil;
import org.roc.wim.entityLinking.caculateModel.CommonMath;
import org.springframework.stereotype.Service;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 20:17
 * Desc: 编辑距离相似度
 */
@Service
public class EditDistanceSimilarityModel {
    public float calcSimilarity(String mention, String title) {
        int mentionLength = StringUtil.isEmpty(mention) ? 0 : mention.length();
        int titleLength = StringUtil.isEmpty(title) ? 0 : title.length();
        int editDistance =  CommonMath.calcEditDistance(mention, title, false);
        return mentionLength+editDistance==titleLength?1.0f:0.0f;
    }
}
