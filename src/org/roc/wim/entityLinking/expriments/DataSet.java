package org.roc.wim.entityLinking.expriments;

import com.roc.core.base.BaseDTO;
import org.roc.wim.entityLinking.el.mention.Mention;
import org.roc.wim.entityLinking.el.mention.MentionBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: rocwu
 * Date: 2016/12/19
 * Time: 19:48
 * Desc: 数据集
 */
@Component
public class DataSet {

    private static final int trainDocCount      = 993;
    private static final int validateDocCount   = 200;
    private static final int testDocCount       = 200;

    @Autowired
    private MentionBL mentionBL;

    private List<Mention> trainMentions = new ArrayList<>();
    private List<Mention> validateMentions = new ArrayList<>();
    private List<Mention> testMentions = new ArrayList<>();

    public DataSet() {

    }

    @PostConstruct
    private void init() {
        List<BaseDTO> list = mentionBL.getListByCondition(Mention.DocId+"<='"+trainDocCount+"' and "+Mention.NeType+" != 'MISC' and "+Mention.EntityId+" > '0'");
        trainMentions.addAll(list.stream().map(item -> (Mention) item).collect(Collectors.toList()));
        list = mentionBL.getListByCondition(Mention.DocId+">'"+trainDocCount+"' and "+Mention.DocId+" <= '"+(trainDocCount+validateDocCount)+"' and "+Mention.NeType+" != 'MISC' and "+Mention.EntityId+" > '0'");
        validateMentions.addAll(list.stream().map(item -> (Mention) item).collect(Collectors.toList()));
        list = mentionBL.getListByCondition(Mention.DocId+">'"+(trainDocCount+validateDocCount)+"' and "+Mention.NeType+" != 'MISC' and "+Mention.EntityId+" > '0'");
        testMentions.addAll(list.stream().map(item -> (Mention) item).collect(Collectors.toList()));
    }

    public List<Mention> getTrainMentions() {
        return trainMentions;
    }

    public List<Mention> getValidateMentions() {
        return validateMentions;
    }

    public List<Mention> getTestMentions() {
        return testMentions;
    }
}
