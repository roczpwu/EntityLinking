package org.roc.wim.entityLinking.el.doc;

import com.roc.core.base.BaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rocwu
 * Date: 2016/12/25
 * Time: 21:45
 * Desc: Doc cache
 */
@Component
public class DocCache {

    @Autowired
    private DocBL docBL;

    private Map<Integer, Doc> docMap = new HashMap<>();

    @PostConstruct
    private void init() {
        for(BaseDTO item : docBL.getList()) {
            Doc doc = (Doc) item;
            docMap.put(doc.getId(), doc);
        }
    }

    public Doc get(int key) {
        return docMap.get(key);
    }
}
