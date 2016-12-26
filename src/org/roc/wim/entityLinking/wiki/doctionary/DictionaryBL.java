package org.roc.wim.entityLinking.wiki.doctionary;

import com.roc.core.Utils.StringUtil;
import com.roc.core.base.*;
import org.roc.wim.entityLinking.wiki.page.*;
import org.roc.wim.entityLinking.wiki.page.Page;
import org.roc.wim.entityLinking.wiki.redirect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/23
 * Time: 19:37
 * Desc: google词典业务逻辑
 */
@Service
public class DictionaryBL extends BaseBL {

    public static final int CandidateCount = 10;

    @Autowired
    private DictionaryDAO dictionaryDAO;
    @Autowired
    private CandidateBL candidateBL;
    @Autowired
    private PageBL pageBL;
    @Autowired
    private RedirectBL redirectBL;
    @Autowired
    private Title2IdCache title2IdCache;

    @PostConstruct
    public void init() {
        this.dao = dictionaryDAO;
    }

    /**
     * 根据指称获取候选实体列表
     * @param name 指称
     * @return 候选实体列表
     */
    public List<Candidate> getCandidateTitles(String name) {
        List<Candidate> result = new ArrayList<>();
        // 先从el.entityname中查
        List entityNameList = candidateBL.getListByCondition(Candidate.Name + " = '"+name+"'");
        if (entityNameList.size() > 0) {
            Collections.sort(entityNameList, (o1, o2) -> ((Candidate)o1).getProbOfNameToEntity()-((Candidate)o2).getProbOfNameToEntity()>0.0f?-1:1);
            if (entityNameList.size()>CandidateCount) entityNameList = entityNameList.subList(0, CandidateCount);
            return entityNameList;
        }
        // el.entityname中不存在，则直接从wiki.dictionary中查
        dictionaryDAO.where(Dictionary.Name + "='"+ StringUtil.mysqlEscapeStr(name)+"'");
        List<BaseDTO> dictionaryList = dictionaryDAO.all();
        Set<String> pageTitleSet = new HashSet<>();
        for (BaseDTO item : dictionaryList) {
            Dictionary dictionary = (Dictionary) item;
            Page targetPage = null;
            Page page = pageBL.get(0, dictionary.getWikiTitle());
            if (page != null) {
                Redirect redirect = (Redirect) redirectBL.get(page.getPage_id());
                if (redirect != null) {
                    targetPage = (Page) pageBL.get(redirect.getRd_page_id());
                }
            }
            if (targetPage != null) {
                dictionary.setWikiTitle(targetPage.getPage_title());
            }
            if (!pageTitleSet.contains(dictionary.getWikiTitle())) {
                Candidate candidate = new Candidate();
                candidate.setName(dictionary.getName());
                candidate.setWikiTitle(dictionary.getWikiTitle());
                candidate.setProbOfNameToEntity(dictionary.getProbOfNameToEntity());
                Integer entityId = title2IdCache.get(dictionary.getWikiTitle());
                if (entityId == null) continue;
                candidate.setEntityId(entityId);
                result.add(candidate);
                pageTitleSet.add(dictionary.getWikiTitle());
            }
        }
        Collections.sort(result, (o1, o2) -> o1.getProbOfNameToEntity()-o2.getProbOfNameToEntity()>0.0f?-1:1);
        for (int i = 0; i < result.size(); i++)
            result.get(i).setSeq(i);
        candidateBL.save(result);
        if (result.size()>CandidateCount) result = result.subList(0, CandidateCount);
        return result;
    }
}
