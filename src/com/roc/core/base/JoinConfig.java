package com.roc.core.base;

/**
 * Created with IntelliJ IDEA.
 * User: lutingming
 * Date: 13-9-22
 * Time: 下午4:24
 * Desc: 数据库表连接配置
 */
public class JoinConfig {
    // 表名
    protected String tableName;
    // 表别名
    protected String tableAlias;
    // 连接条件
    protected String condition;

    public JoinConfig(String tableName, String tableAlias, String condition) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.condition = condition;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
