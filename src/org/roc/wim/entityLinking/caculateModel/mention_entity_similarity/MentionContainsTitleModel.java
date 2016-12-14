package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import com.roc.core.Utils.StringUtil;
import org.springframework.stereotype.Service;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 19:23
 * Desc: 计算mention是否包含实体标题
 */
@Service
public class MentionContainsTitleModel {
    public float calcSimilarity(String mention, String title) {
        if (StringUtil.isEmpty(mention) || StringUtil.isEmpty(title))
            return 0.0f;
        mention = mention.toUpperCase();
        title = title.toUpperCase();
        return mention.contains(title) ? 1.0f : 0.0f;
    }
}
