package org.roc.wim.entityLinking.wiki.entitytoentitylink;

import com.roc.core.base.BaseBL;
import com.roc.core.base.BaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 16:26
 * Desc: 实体之间链接关系业务逻辑
 */
@Service
public class EntitytoentitylinkBL extends BaseBL {

    @Autowired
    private EntitytoentitylinkDAO entitytoentitylinkDAO;

    @PostConstruct
    public void init() {
        this.dao = entitytoentitylinkDAO;
    }

    public Set<Integer> getFromEntityListByToEntity(int entityId) {
        dao.where(Entitytoentitylink.ToEntityId+"='"+entityId+"'");
        List<BaseDTO> list = dao.all();
        Set<Integer> result = new HashSet<>();
        for (BaseDTO dto : list) {
            result.add(((Entitytoentitylink) dto).getFromEntityId());
        }
        return result;
    }
}
