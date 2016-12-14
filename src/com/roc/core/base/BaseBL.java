package com.roc.core.base;

import org.apache.logging.log4j.*;

import java.sql.SQLException;
import java.util.List;

/**
 * User: rocwu
 * Date: 2016/08/4
 * Time: 10:45 pm
 * Desc: 数据库业务逻辑层
 */
public abstract class BaseBL {

    // 日志工具
    private Logger logger = LogManager.getLogger(this.getClass().getName());

    // DAO
    protected BaseDAO dao = null;

    /**
     * 由key得到一条记录
     * @param key key
     * @return BaseDTO
     */
    public BaseDTO get(Object... key) {
        return dao.getByKey(key);
    }

    /**
     * 由condition得到一条记录
     * @param condition
     * @return
     */
    public BaseDTO getByCondition(Object condition) {
        return dao.where(condition).limit(1).one();
    }

    /**
     * 得到多条记录
     * @param page
     */
    public void getList(Page page) throws Exception {
        dao.page(page);
    }

    /**
     * 得到多条记录
     */
    public List getList() {
        return dao.all();
    }


    /**
     * 得到多条记录
     */
    public List getListByCondition(Object condition) {
        return dao.where(condition).all();
    }

    /**
     *  计数
     */
    public int getCount() {
        return dao.count();
    }

    /**
     * 按条件计数
     * @param condition
     */
    public int getCount(Object condition) throws SQLException {
        return dao.where(condition).count();
    }

    /**
     * 插入/更新一条记录
     * 取决于DTO状态
     */
    public int save(BaseDTO dto) {
        return dao.save(dto);
    }

    /**
     * 插入/更新多条记录
     * 取决于DTO状态
     */
    public int save(List<BaseDTO> dtoList) {
        return dao.save(dtoList);
    }

    /**
     * 根据key删除一条记录
     */
    public int delete(Object... key) throws Exception {
        return dao.deleteByKey(key);
    }

    /**
     * 删除一条或多条记录
     */
    public int delete(Object condition) throws Exception {
        return dao.where(condition).delete();
    }

    public int delete(BaseDTO dto) {
        return dao.delete(dto);
    }

    /**
     * 删除所有记录
     */
    public int deleteAll() {
        return dao.where(null).delete();
    }
}
