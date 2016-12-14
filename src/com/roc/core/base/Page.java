package com.roc.core.base;

import com.roc.core.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * User: Lu Tingming
 * Time: 2010-01-5
 * Desc: 分页查询中的一页
 */
public class Page {

    public static final int MAXRECORDNUMPERPAGE = 200;
    /**
     * 记录列表
     */
    protected List list;

    /**
     * 一页中的记录数
     */
    protected int recordNumPerPage = 10;

    /**
     * 当前页码（从1开始计数）
     */
    protected int currentPageNo = 1;

    /**
     * 总记录数
     */
    protected int totalRecordNum;

    /**
     * 查询字段
     */
    protected String fileds;

    /**
     * 排序字段
     */
    protected String orderBy;

    /**
     * 查询条件
     */
    protected Object whereCondition;

    protected List<JoinConfig> joinConfigList;


    public Page() {
    }


    public Page(int recordNumPerPage) {
        this.recordNumPerPage = recordNumPerPage;
    }

    /**
     * 得到记录表
     *
     * @return 记录表
     */
    public List getList() {
        return list;
    }

    /**
     * 设置记录表
     *
     * @param list 记录表
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * 得到一页中的记录数
     *
     * @return 一页中的记录数
     */
    public int getRecordNumPerPage() {
        return recordNumPerPage;
    }

    /**
     * 设置一页中的记录数
     *
     * @param recordNumPerPage 一页中的记录数
     */
    public void setRecordNumPerPage(int recordNumPerPage) {
        if (recordNumPerPage == 0) {
            this.recordNumPerPage = 20;
        } else {
            this.recordNumPerPage = recordNumPerPage;
        }

    }

    /**
     * 设置一页中的记录数
     * 用于处理用户输入非法数据
     * 如果用户输入的数据不是数字类型则会调用此方法 将
     *
     * @param recordNumPerPage 一页中的记录数
     */
    public void setRecordNumPerPage(String recordNumPerPage) {
        int value = 20;
        if (!StringUtil.isEmpty(recordNumPerPage)) {
            try {
                value = Integer.parseInt(recordNumPerPage);
            } catch (Exception e) {
                value = 20;
            }
        }
        //如果输入的数字为0将其转化为20
        if (value <= 0 || value > 200) {
            value = 20;
        }
        this.recordNumPerPage = value;
    }

    /**
     * 得到当前页码
     *
     * @return 当前页码
     */
    public int getCurrentPageNo() {
        return currentPageNo;
    }

    /**
     * 设置当前页码
     *
     * @param currentPageNo 当前页码
     */
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    /**
     * 得到总记录数
     *
     * @return 总记录数
     */
    public int getTotalRecordNum() {
        return totalRecordNum;
    }

    /**
     * 设置总记录数
     *
     * @param totalRecordNum 总记录数
     */
    public void setTotalRecordNum(int totalRecordNum) {
        this.totalRecordNum = totalRecordNum;
    }

    /**
     * 得到总页数
     *
     * @return 总页数
     */
    public int getTotalPageNum() {
        return ((totalRecordNum - 1) / recordNumPerPage) + 1;
    }

    /**
     * 得到查询字段
     *
     * @return 总页数
     */
    public String getFileds() {
        return fileds;
    }

    /**
     * 设置查询字段
     *
     * @param fileds 查询字段
     */
    public void setFileds(String fileds) {
        this.fileds = fileds;
    }

    /**
     * 得到排序方式
     *
     * @return 排序方式
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序方式
     *
     * @param orderBy 排序方式
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 得到查询条件
     *
     * @return 查询条件
     */
    public Object getWhereCondition() {
        return whereCondition;
    }

    /**
     * 设置查询条件
     *
     * @param whereCondition 查询条件
     */
    public void setWhereCondition(Object whereCondition) {
        this.whereCondition = whereCondition;
    }

    /**
     * 获取当前页的开始记录序号（从0编号）
     *
     * @return
     */
    public int getStartRecordIndex() {
        int start = (currentPageNo - 1) * recordNumPerPage;
        return (start > totalRecordNum) ? totalRecordNum - 1 : start;
    }

    /**
     * 获取当前页的结束记录序号（从0编号）
     *
     * @return
     */
    public int getEndRecordIndex() {
        int end = currentPageNo * recordNumPerPage - 1;
        return (end > totalRecordNum) ? totalRecordNum - 1 : end;
    }


    /**
     * 返回当前页的记录数
     */
    public int getRecordNumInCurrentPage() {
        return getEndRecordIndex() - getStartRecordIndex() + 1;
    }


    /**
     * 如果当前页码大于总页码，则当前页码设为总页码
     */
    public void checkCurrentPageNo() {
        int totalPageNum = getTotalPageNum();
        if (currentPageNo > totalPageNum) {
            currentPageNo = totalPageNum;
        }
    }


    /**
     * 增加一个表连接设置
     */
    public void addJoinConfig(JoinConfig joinConfig) {
        if(joinConfigList == null) {
            joinConfigList = new ArrayList<JoinConfig>();
        }
        joinConfigList.add(joinConfig);
    }


    public List<JoinConfig> getJoinConfigList() {
        return joinConfigList;
    }

    public void setJoinConfigList(List<JoinConfig> joinConfigList) {
        this.joinConfigList = joinConfigList;
    }

    /**
     * 获得表连接SQL
     */
    public String getJoinSql() {
        if (this.getJoinConfigList() == null || this.getJoinConfigList().size() == 0) {
            return null;
        }

        StringBuilder joinSql = new StringBuilder();
        for (JoinConfig joinConfig : this.getJoinConfigList()) {
            // 表名
            joinSql.append(" JOIN ").append(joinConfig.getTableName());
            // 别名
            if (StringUtil.notEmpty(joinConfig.getTableAlias())) {
                joinSql.append(" ").append(joinConfig.getTableAlias());
            }
            // 条件
            if (StringUtil.notEmpty(joinConfig.getCondition())) {
                joinSql.append(" ON (").append(joinConfig.getCondition()).append(")");
            }
        }

        return joinSql.toString();
    }

    public String getUrlCondition() {
        StringBuilder sb = new StringBuilder();
        sb.append("page.recordNumPerPage=").append(this.getRecordNumInCurrentPage());
        sb.append("&").append("page.currentPageNo=").append(this.getCurrentPageNo());
        if (this.getOrderBy() != null)
            sb.append("&").append("page.orderBy=").append(this.getOrderBy());
        if (this.getWhereCondition() != null)
            sb.append("&").append("page.whereCondition=").append(this.getWhereCondition());
        return sb.toString();
    }
}
