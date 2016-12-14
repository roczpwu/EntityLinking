package org.roc.wim.entityLinking.caculateModel.mention_entity_similarity;

import com.roc.core.Utils.StringUtil;
import org.springframework.stereotype.Service;

/**
 * User: rocwu
 * Date: 2016/11/24
 * Time: 19:32
 * Desc: 计算实体标题是否包含mention
 */
@Service
public class TitleContainsMentionModel {
    public float calcSimilarity(String mention, String title) {
        if (StringUtil.isEmpty(mention) || StringUtil.isEmpty(title))
            return 0.0f;
        mention = mention.toUpperCase();
        title = title.toUpperCase();
        return title.contains(mention) ? 1.0f : 0.0f;
    }
}
