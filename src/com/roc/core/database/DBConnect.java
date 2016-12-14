package com.roc.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * User: Lu Tingming
 * Date: 2010-11-12 12:42:59
 * Desc: 数据库连接类
 */
public class DBConnect {
    private String configName;
    // 连接
    private Connection conn;
    // 是否批量操作
    protected boolean isBatch = false;


    /**
     * constructor
     */
    public DBConnect() {
        conn = getConnection();// 从本机获取数据库链接
    }


    /**
     * constructor
     */
    public DBConnect(String configName) {
        this.configName = configName;
        conn = getConnection();// 从本机获取数据库链接
    }


    /**
     * 获取数据库连接
     */
    private Connection getConnection() {
        return DBCPManager.getConnection(configName);
    }


    /**
     * 执行查询
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public synchronized ResultSet executeQuery(String sql) throws SQLException {
        try {
            if (conn == null) {
                conn = getConnection();
            }

            PreparedStatement preStatement = conn.prepareStatement(sql);
            ResultSet rs = preStatement.executeQuery();
            // preStatement.close();
            return rs;
        } catch (SQLException e) {
            System.err.println(sql);
            throw e;
        }
    }

    /**
     * 执行查询
     */
    public synchronized ResultSet executeQuery(String sql, List list) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }

        PreparedStatement preStatement = conn.prepareStatement(sql);
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DBConnect.setParameter(preStatement, i + 1, list.get(i));
                setParameter(preStatement, i + 1, list.get(i));
            }
        }
        ResultSet rs = preStatement.executeQuery();
        // preStatement.close();
        return rs;
    }

    /**
     * 执行查询
     *
     * @param sql
     * @param objects
     * @return
     * @throws SQLException
     */
    public synchronized ResultSet executeQuery(String sql, Object[] objects) {
        if (conn == null) {
            conn = getConnection();
        }

        try {
        PreparedStatement preStatement = conn.prepareStatement(sql);
        if (objects != null) {
            int size = objects.length;
            for (int i = 0; i < size; i++) {
                setParameter(preStatement, i + 1, objects[i]);
            }
        }
        ResultSet rs = preStatement.executeQuery();
        // preStatement.close();
        return rs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行查询
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public synchronized ResultSet executeQueryByParams(String sql, Object... params) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }

        PreparedStatement preStatement = conn.prepareStatement(sql);
        if (params != null) {
            int size = params.length;
            for (int i = 0; i < size; i++) {
                setParameter(preStatement, i + 1, params[i]);
            }
        }
        ResultSet rs = preStatement.executeQuery();
        // preStatement.close();
        return rs;
    }

    /**
     * 执行查询
     *
     * @param sql
     * @param param
     * @return
     * @throws SQLException
     */
    public synchronized ResultSet executeQuery(String sql, Object param) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }

        PreparedStatement preStatement = conn.prepareStatement(sql);
        setParameter(preStatement, 1, param);
        ResultSet rs = preStatement.executeQuery();
        // preStatement.close();
        return rs;
    }


    /**
     * 执行更新
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public synchronized int update(int maxEffectedRows, String sql) throws Exception {
        boolean originalAutoCommit = this.getAutoCommit();

        if (originalAutoCommit) {
            this.setAutoCommit(false);
        }

        int effectedRows = update(sql, (List) null);
        if (effectedRows > maxEffectedRows) {
            this.rollback();
            if (originalAutoCommit) {
                this.setAutoCommit(true);
            }
            throw new Exception("effectedRows[" + effectedRows + ">maxEffectedRows[" + maxEffectedRows + "]");
        }

        return effectedRows;
    }

    /**
     * 执行更新
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public synchronized int update(String sql) throws SQLException {
        return update(sql, (List) null);
    }

    /**
     * 执行更新
     *
     * @param sql
     * @param paramList
     * @return
     * @throws SQLException
     */
    public synchronized int update(String sql, List paramList) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }
        PreparedStatement preStatement = conn.prepareStatement(sql);

        if (paramList != null) {
            int size = paramList.size();
            for (int i = 0; i < size; i++) {
                //设置参数值
                setParameter(preStatement, i + 1, paramList.get(i)); }
        }

        int ret = preStatement.executeUpdate();

        preStatement.close();
        return ret;
    }


    /**
     * 执行更新
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public synchronized int update(String sql, Object... params) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }
        PreparedStatement preStatement = conn.prepareStatement(sql);

        if (params != null && params.length > 0) {
            int size = params.length;
            for (int i = 0; i < size; i++) {
                //设置参数值
                setParameter(preStatement, i + 1, params[i]);
            }
        }

        int ret = preStatement.executeUpdate();

        preStatement.close();
        return ret;
    }


    /**
     * 执行更新
     *
     * @param list
     * @return
     * @throws SQLException
     */
    public synchronized int update(PreparedStatement preStatement, List list) throws SQLException {
        if (conn == null) {
            conn = getConnection();
        }

        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                //设置参数值
                DBConnect.setParameter(preStatement, i + 1, list.get(i));
                setParameter(preStatement, i + 1, list.get(i));
            }
        }

        int ret = preStatement.executeUpdate();

        return ret;
    }


    /**
     * 名称：close()
     * 功能：释放连接
     * 参数：无
     * 返回值：无
     */
    public void close() {
        // 如果是批量处理，那么不需要关闭
        // 由endBatch关闭
        if (isBatch) return;

        try {
            if (conn != null) {
                // 如果不是自动提交，那么等提交后再关闭
                if (!conn.getAutoCommit()) {
                    return;
                }
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 设置自动提交
     */
    public void setAutoCommit(boolean autoCommit) {
        // 如果是批量处理模式，那么不可以修改自动提交值
        if (isBatch) {
            return;
        }

        try {
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean getAutoCommit() {
        try {
            return conn.getAutoCommit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 提交
     */
    public void commit() throws SQLException {
        // 如果是批量处理模式，那么不可以在此提交
        // 而由commitBatch提交
        if (isBatch) {
            return;
        }

        conn.commit();
    }


    /**
     * 回滚
     */
    public void rollback() throws SQLException {
        conn.rollback();
    }


    /**
     * 得到准备语句
     */
    public PreparedStatement prepareStatement(String sql) {
        if (conn == null) {
            conn = getConnection();
        }
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 启用批量处理模式
     */
    public void beginBatch() throws SQLException {
        isBatch = true;

        // 关闭自动提交
        conn.setAutoCommit(false);
    }

    /**
     * 结束批量处理模式
     */
    public void commitBatch() throws SQLException {
        conn.commit();
        conn.close();
        conn = null;

        isBatch = false;
    }


    /**
     * 回滚批量处理模式
     */
    public void rollbackBatch() throws SQLException {
        conn.rollback();
        conn.close();
        conn = null;

        isBatch = false;
    }


    public static void setParameter(PreparedStatement ps, int idx, Object value) {
        try{
        if (value != null) {
            if (value.getClass().equals(Character.class)) {
                ps.setObject(idx, value.toString());
            } else if (value.getClass().equals(char[].class)) {
                ps.setString(idx, new String((char[]) value));
            } else if (value.getClass().equals(byte[].class)) {
                ps.setBytes(idx, (byte[]) value);
            } else {
                ps.setObject(idx, value);
            }
        } else {
            ps.setObject(idx, value);
        }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
