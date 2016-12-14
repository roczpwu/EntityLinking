package org.roc.wim.entityLinking.wiki.entitytoentitylink;

import com.roc.core.base.BaseDTO;

/**
 * User: rocwu
 * Date: 2016/11/19
 * Time: 19:55
 * Desc: 实体link信息
 *          用于计算entity之间的相关性
 */
public class Entitytoentitylink extends BaseDTO {

    public static final String FromEntityId = "fromEntityId";
    public static final String ToEntityId = "toEntityId";

    public Entitytoentitylink() {
        this.primaryKey = new String[2];
        this.primaryKey[0] = Entitytoentitylink.FromEntityId;
        this.primaryKey[1] = Entitytoentitylink.ToEntityId;
        this.isAutoIncrease = false;
    }

    private int fromEntityId;
    private int toEntityId;

    public int getFromEntityId() {
        return fromEntityId;
    }

    public void setFromEntityId(int fromEntityId) {
        this.fromEntityId = fromEntityId;
    }

    public int getToEntityId() {
        return toEntityId;
    }

    public void setToEntityId(int toEntityId) {
        this.toEntityId = toEntityId;
    }
}
