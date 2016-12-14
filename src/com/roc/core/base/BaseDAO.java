package com.roc.core.base;

import com.roc.core.Utils.*;
import com.roc.core.database.*;
import org.apache.logging.log4j.*;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

/**
 * User: rocwu
 * Date: 2016/08/4
 * Time: 10:45 pm
 * Desc: 数据库操作层
 */
public abstract class BaseDAO {

    protected Class dtoType = null;

    protected String dbName;
    protected String tableName;
    protected String dbConnectConfig = null;

    private DBConnect dbConnect = null;

    private String fields = "*";
    private String condition = null;
    private int limit = -1;
    private int offset = -1;
    private String orderBy = null;
    private String groupBy = null;
    private String joinSql = null;

    private Logger logger = LogManager.getLogger(this.getClass().getName());

    public BaseDAO() {
    }

    public BaseDAO(DBConnect dbConnect) throws Exception {
        if (dbConnect == null)
            throw new Exception("dbConnect can't be null");
        this.dbConnect = dbConnect;
    }

    public BaseDAO(String dbConnectConfig) {
        this.dbConnect = new DBConnect(dbConnectConfig);
    }

    /**
     * 设置查询字段
     * @param s 字段列表字符串
     * @return this 方便链式传递
     */
    public BaseDAO fields(String s) {
        this.fields = s;
        return this;
    }

    /**
     * 设置查询字段
     * @param fieldsArr 字段列表数组
     * @return this 方便链式传递
     */
    public BaseDAO fields(String[] fieldsArr) {
        StringBuilder sb = new StringBuilder();
        for (String field : fieldsArr) {
            sb.append(field);
            sb.append(", ");
        }
        if (sb.length() > 0)
            sb.substring(0, sb.length() - 2);
        this.fields = sb.toString();
        return this;
    }

    /**
     * 设置查询条件
     * @param obj 见Condition
     * @return
     */
    public BaseDAO where(Object obj) {
        this.condition = Condition.where(obj);
        return this;
    }

    /**
     * join
     * @param joinSql
     * @return
     */
    public BaseDAO join(String joinSql) {
        this.joinSql = joinSql;
        return this;
    }

    /**
     * limit限制，取消限制设负值
     * @param limit
     * @return
     */
    public BaseDAO limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * offset限制，取消限制设负值
     * @param offset
     * @return
     */
    public BaseDAO offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 排序设置
     * @param orderBy
     * @return
     */
    public BaseDAO orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    /**
     * 分组设置
     * @param groupBy
     * @return
     */
    public BaseDAO groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    /**
     * 根据key获取DTO
     * @param obj
     * @return
     */
    public BaseDTO getByKey(Object... obj) {
        try {
            BaseDTO dto = (BaseDTO) this.dtoType.newInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("select * from ");
            sb.append(this.dbName).append(".").append(this.tableName);
            sb.append(" where ");
            for (int i = 0; i < dto.primaryKey.length; i++) {
                sb.append(dto.primaryKey[i]);
                sb.append("=").append(DBUtil.convert(StringUtil.mysqlEscapeStr(obj[i].toString())));
                sb.append(" and ");
            }
            sb = sb.delete(sb.length()-5, sb.length());
            sb.append(" limit 1");
            String sql = sb.toString();
            this.dbConnect = this.getDBConnect();
            ResultSet rs = this.dbConnect.executeQuery(sql);
            if (!rs.next()) return null;
            DBUtil.result2Bean(rs, dto);
            dto.updateKeyCondition();
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    /**
     * 查询第一条符合条件的记录
     * @return dto
     */
    public BaseDTO one() throws RuntimeException {
        this.limit = 1;
        try {
            String sql = this.getQuerySql();
            ResultSet rs = this.getDBConnect().executeQuery(sql);
            if (rs.next()) {
                BaseDTO dto = (BaseDTO) dtoType.newInstance();
                DBUtil.result2Bean(rs, dto);
                dto.updateKeyCondition();
                return dto;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
        return null;
    }

    /**
     * 获取所有符合条件的查询结果
     * @return dto list
     * @throws Exception
     */
    public List<BaseDTO> all() throws RuntimeException {
        try {
            String sql =this.getQuerySql();
            ResultSet rs = this.getDBConnect().executeQuery(sql);
            List<BaseDTO> list = new ArrayList<>();
            while (rs.next()) {
                BaseDTO dto = (BaseDTO) this.dtoType.newInstance();
                DBUtil.result2Bean(rs, dto);
                dto.updateKeyCondition();
                list.add(dto);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    /**
     * 删除一条记录
     * @param dto
     * @return
     */
    public int delete(BaseDTO dto) {
        try {
            String sql = this.getDeleteSql(dto);
            int affectedRowNum = this.getDBConnect().update(sql);
            return affectedRowNum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    /**
     * 删除多条记录
     * @return
     */
    public int delete() {
        try {
            String sql = this.getDeleteSql();
            return this.getDBConnect().update(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    public int deleteByKey(Object... obj) {
        try {
            BaseDTO dto = (BaseDTO) this.dtoType.newInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("delete from ");
            sb.append(this.dbName).append(".").append(this.tableName);
            sb.append(" where ");
            for (int i = 0; i < dto.primaryKey.length; i++) {
                sb.append(dto.primaryKey[i]);
                obj[i] = StringUtil.mysqlEscapeStr((String)obj[i]);
                sb.append("=").append(DBUtil.convert(obj[i]));
                sb.append(" and ");
            }
            sb = sb.delete(sb.length()-5, sb.length());
            String sql = sb.toString();
            this.dbConnect = this.getDBConnect();
            return this.dbConnect.update(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    /**
     * 获取页
     * @param page
     * @return
     */
    public Page page(Page page) {
        ResultSet res = null;
        try {
            // 得到总记录数
            // 查询条件
            this.where(page.getWhereCondition());
            int totalRecordNum = this.count();
            page.setTotalRecordNum(totalRecordNum);
            // 如果当前页码大于总页码，则当前页码设为总页码
            if (page.getCurrentPageNo() > page.getTotalPageNum()) {
                page.setCurrentPageNo(page.getTotalPageNum());
            }
            // 检查每页记录大小，上限为200
            if (page.getRecordNumPerPage() > Page.MAXRECORDNUMPERPAGE)
                page.setRecordNumPerPage(Page.MAXRECORDNUMPERPAGE);
            // 取记录
            // 查询条件
            this.where(page.getWhereCondition());
            // 表连接
            this.join(page.getJoinSql());
            // 分页
            int start = page.getRecordNumPerPage() * (page.getCurrentPageNo() - 1);
            this.limit(page.getRecordNumPerPage());
            this.offset(start);
            // 排序
            this.orderBy(page.getOrderBy());
            // 获取当前页结果
            List<BaseDTO> list = this.all();
            page.setList(list);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return page;
    }

    /**
     * 获取记录数
     * @return
     * @throws Exception
     */
    public int count() throws RuntimeException {
        try {
            String sql =this.getCountSql();
            ResultSet rs = this.getDBConnect().executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
    }

    /**
     * 保存/创建一条记录
     * isRelatedWithDB
     * @return
     */
    public int save(BaseDTO dto) throws RuntimeException {
        int affectRowNum = 0;
        try {
            this.dbConnect = this.getDBConnect();
            if (dto.getIsRelatedWithDB()) {
                String sql = this.getUpdateSql(dto);
                List<Object> paramList = new ArrayList<>();
                setParametersForUpdate(paramList, dto);
                affectRowNum += this.dbConnect.update(sql, paramList);
            } else {
                String sql = this.getInsertSql(dto);
                List<Object> paramList = new ArrayList<>();
                setParametersForInsert(paramList, dto);
                affectRowNum += this.dbConnect.update(sql, paramList);
                if (affectRowNum > 0) {
                    Field relatedField = BaseDTO.class.getDeclaredField("isRelatedWithDB");
                    relatedField.setAccessible(true);
                    relatedField.set(dto, true);
                    if (dto.isAutoIncrease) {
                        ResultSet rs = this.dbConnect.executeQuery("SELECT LAST_INSERT_ID()");
                        rs.next();
                        int key = rs.getInt(1);
                        Method setMethod = dto.getClass().getMethod("set" + StringUtil.upperInitial(dto.primaryKey[0]), int.class);
                        setMethod.invoke(dto, key);
                    }
                    dto.updateKeyCondition();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
        return affectRowNum;
    }

    /**
     * 批量 保存/创建 记录
     * @param dtoList
     * @return
     */
    public int save(List<BaseDTO> dtoList) {
        if (dtoList == null || dtoList.size() == 0) return 0;
        int affectRowNum = 0;
        this.dbConnect = this.getDBConnect();
        boolean preCommitType = this.dbConnect.getAutoCommit();
        this.dbConnect.setAutoCommit(false);

        try {
            List<List<Object>> paramList = new ArrayList<List<Object>>();
            for (BaseDTO dto : dtoList) {
                List<Object> list = new ArrayList<Object>();
                if (dto.getIsRelatedWithDB())
                    this.setParametersForUpdate(list, dto);
                else
                    this.setParametersForInsert(list, dto);
                paramList.add(list);
            }
            String sqlForInsert = this.getInsertSql((BaseDTO) this.dtoType.newInstance());
            // 准备SQL
            PreparedStatement ps = dbConnect.prepareStatement(sqlForInsert);
            // 执行
            for (int i = 0; i < paramList.size(); i++) {
                if (i > 0 && i % 10000 == 0) {
                    logger.debug("update " + (i + 1) + "/" + paramList.size());
                }
                if (dtoList.get(i).getIsRelatedWithDB()) {
                    PreparedStatement psForUpdate = dbConnect.prepareStatement(this.getUpdateSql(dtoList.get(i)));
                    affectRowNum += dbConnect.update(psForUpdate, paramList.get(i));
                } else {
                    try {
                        affectRowNum += dbConnect.update(ps, paramList.get(i));
                        if (dtoList.get(i).isAutoIncrease) {
                            ResultSet rs = this.dbConnect.executeQuery("SELECT LAST_INSERT_ID()");
                            rs.next();
                            int key = rs.getInt(1);
                            Method setMethod = dtoList.get(i).getClass().getMethod("set" + StringUtil.upperInitial(dtoList.get(i).primaryKey[0]), int.class);
                            setMethod.invoke(dtoList.get(i), key);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                dtoList.get(i).updateKeyCondition();
            }
            this.dbConnect.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnect();
        }
        this.dbConnect.setAutoCommit(preCommitType);
        return affectRowNum;
    }

    /**
     * 获取查询sql
     * @return
     */
    private String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ").append(this.fields).append(" from ");
        sb.append(this.dbName).append(".").append(this.tableName).append(" ");
        if (!StringUtil.isEmpty(this.condition))
            sb.append("where ( ").append(this.condition).append(" ) ");
        if (!StringUtil.isEmpty(this.joinSql))
            sb.append(this.joinSql).append(" ");
        if (!StringUtil.isEmpty(this.orderBy))
            sb.append("order by ").append(this.orderBy).append(" ");
        if (!StringUtil.isEmpty(this.groupBy))
            sb.append("group by ").append(this.groupBy).append(" ");
        if (this.limit >= 0)
            sb.append("limit ").append(this.limit).append(" ");
        if (this.offset >= 0)
            sb.append("offset ").append(this.offset).append(" ");
        return sb.toString();
    }

    /**
     * 获取删除sql
     * @return
     */
    private String getDeleteSql(BaseDTO dto) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.dbName).append(".").append(this.tableName).append(" ");
        sb.append(" where ");
        sb.append(dto.getKeyCondition());
        return sb.toString();
    }

    /**
     * 获取删除sql
     * @return
     */
    private String getDeleteSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.dbName).append(".").append(this.tableName).append(" ");
        if (!StringUtil.isEmpty(this.condition)) {
            sb.append("where ").append(this.condition);
        }
        return sb.toString();
    }

    /**
     * 获取计数sql
     * @return
     */
    private String getCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*)").append(" from ");
        sb.append(this.dbName).append(".").append(this.tableName).append(" ");
        if (!StringUtil.isEmpty(condition))
            sb.append(this.condition).append(" ");
        return sb.toString();
    }

    /**
     * 获取update sql
     * @param dto dto
     * @return sql
     * @throws Exception
     */
    private String getUpdateSql(BaseDTO dto) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(this.dbName).append(".").append(this.tableName).append(" ");
        sb.append(" set ");
        String[] fieldNames = FieldsMap.getFields(this);
        for (String fieldName : fieldNames) {
            sb.append(fieldName).append("=?").append(", ");
        }
        sb = sb.delete(sb.length()-2, sb.length());
        sb.append(" where ").append(dto.getKeyCondition());
        return sb.toString();
    }

    /**
     * 获取insert sql
     * @param dto dto
     * @return sql
     * @throws Exception
     */
    private String getInsertSql(BaseDTO dto) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(this.dbName).append(".").append(this.tableName).append(" ");
        sb.append("( ");
        String[] fieldNames = FieldsMap.getFields(this);
        int paramCount = 0;
        for (String fieldName : fieldNames) {
            if (ArrayUtil.firstIndexOf(dto.primaryKey, fieldName) >=0 && dto.isAutoIncrease)
                continue;
            sb.append(fieldName).append(", ");
            paramCount++;
        }
        sb = sb.delete(sb.length()-2, sb.length());
        sb.append(" ) values ( ");
        sb.append(StringUtil.duplicate("?, ", paramCount));
        sb = sb.delete(sb.length()-2, sb.length());
        sb.append(" )");
        return sb.toString();
    }

    /**
     * 为插入prepare statement设置参数
     * @param list
     * @param dto
     * @throws Exception
     */
    private void setParametersForInsert(List<Object> list, BaseDTO dto) throws Exception {
        String[] fieldNames = FieldsMap.getFields(this);
        for (String fieldName : fieldNames) {
            if (ArrayUtil.firstIndexOf(dto.primaryKey, fieldName) >=0 && dto.isAutoIncrease)
                continue;
            Method getMethod = dto.getClass().getMethod("get"+StringUtil.upperInitial(fieldName));
            Object obj = getMethod.invoke(dto);
            list.add(obj);
        }
    }

    /**
     * 为更新prepare statement设置参数
     * @param list
     * @param dto
     * @throws Exception
     */
    private void setParametersForUpdate(List<Object> list, BaseDTO dto) throws Exception {
        String[] fieldNames = FieldsMap.getFields(this);
        for (String fieldName : fieldNames) {
            Method getMethod = dto.getClass().getMethod("get"+StringUtil.upperInitial(fieldName));
            Object obj = getMethod.invoke(dto);
            list.add(obj);
        }
    }

    /**
     * 得到数据库连接
     * @return 数据库连接
     */
    public DBConnect getDBConnect() {
        if (this.dbConnect != null) {
            return this.dbConnect;
        }
        if (StringUtil.isEmpty(this.dbConnectConfig))
            this.dbConnect = new DBConnect();
        else
            this.dbConnect = new DBConnect(this.dbConnectConfig);
        return this.dbConnect;
    }

    /**
     * 释放连接，清理BaseDAO
     */
    public void closeConnect() {
        this.fields = "*";
        this.condition = null;
        this.limit = -1;
        this.offset = -1;
        this.orderBy = null;
        this.groupBy = null;
        if (dbConnect.getAutoCommit()) {
            this.dbConnect.close();
            this.dbConnect = null;
        }
    }
}
