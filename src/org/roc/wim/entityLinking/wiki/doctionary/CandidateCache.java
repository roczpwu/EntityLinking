package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.Utils.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: rocwu
 * Date: 2016/12/20
 * Time: 19:52
 * Desc: 候选实体cache
 */
@Component
public class CandidateCache extends BaseCache<String, List<Candidate>> {

    @Autowired
    private DictionaryBL dictionaryBL;

    public CandidateCache() {
        super(10000);
    }

    @Override
    protected List<Candidate> getDirectly(String key) {
        return dictionaryBL.getCandidateTitles(key, true);
    }
}
