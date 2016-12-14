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
    @Autowired
    private DictionaryDAO dictionaryDAO;
    @Autowired
    private PageBL pageBL;
    @Autowired
    private RedirectBL redirectBL;

    @PostConstruct
    public void init() {
        this.dao = dictionaryDAO;
    }

    /**
     * 根据指称获取候选实体列表
     * @param name 指称
     * @return 候选实体列表
     */
    public List<Dictionary> getCandidateTitles(String name) {
        dictionaryDAO.where(Dictionary.NameUpperCase + "='"+ StringUtil.mysqlEscapeStr(name)+"'");
        List<BaseDTO> dictionaryList = dictionaryDAO.all();
        Set<String> pageTitleSet = new HashSet<>();
        List<Dictionary> result = new ArrayList<>();
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
                result.add(dictionary);
                pageTitleSet.add(dictionary.getWikiTitle());
            }
        }
        Collections.sort(result, (o1, o2) -> o1.getProbOfNameToEntity()-o2.getProbOfNameToEntity()>0.0f?-1:1);
        return result;
    }
}