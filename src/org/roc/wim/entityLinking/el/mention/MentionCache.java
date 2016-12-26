package org.roc.wim.entityLinking.el.mention;

import com.roc.core.base.BaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/12/25
 * Time: 21:45
 * Desc: Mention cache
 */
@Component
public class MentionCache {
    private Map<Integer, List<Mention>> mentionMap = new HashMap<>();

    @Autowired
    private MentionBL mentionBL;

    @PostConstruct
    public void init() {
        List<BaseDTO> mentionList = mentionBL.getListByCondition(Mention.EntityId+" >'0' and "+Mention.NeType+" != 'MISC'");
        for(BaseDTO item : mentionList) {
            Mention mention = (Mention) item;
            if (!mentionMap.containsKey(mention.getDocId()))
                mentionMap.put(mention.getDocId(), new ArrayList<>());
            mentionMap.get(mention.getDocId()).add(mention);
        }
        for(int docId : mentionMap.keySet()) {
            Collections.sort(mentionMap.get(docId), (o1, o2) -> o1.getSeqInDoc()-o2.getSeqInDoc());
        }
    }

    public List<Mention> get(int docId) {
        return mentionMap.get(docId);
    }
}
