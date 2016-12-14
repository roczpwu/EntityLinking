package com.roc.core.database;

import com.roc.core.Utils.*;
import org.apache.logging.log4j.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


/**
 * User: Lu Tingming
 * Date: 2010-11-12 12:42:59
 * Desc: 数据库操作工具
 */
public class DBUtil {
    public static boolean isDebug = true;
    private static Logger logger = LogManager.getLogger(DBUtil.class.getName());


    /**
     * 是否存在查询结果
     *
     * @param sql
     * @param params
     * @return Dto
     * @throws SQLException
     */
    public static boolean exists(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;
        try {
            logger.debug(sql);
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            return res.next();
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                    //noinspection UnusedAssignment
                    res = null;
                }
            } catch (SQLException e) {
                logger.error(e);
            }
            try {
                if (dbc != null) {
                    dbc.close();
                    //noinspection UnusedAssignment
                    dbc = null;
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }


    /**
     * 得到列表
     * 根据sql中的列名，填充到实例的相应字段
     * 如果找不到字段，那么异常
     */
    public static ArrayList getObjectListFromTable(String tableName, Class clazz, Object... params) throws Exception {
        return getObjectList("SELECT * FROM " + tableName, clazz, params);
    }

    /**
     * 得到列表
     * 根据sql中的列名，填充到实例的相应字段
     * 如果找不到字段，那么异常
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ArrayList getObjectList(String sql, Class clazz, Object... params) throws Exception {
        logger.debug(sql);

        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            if (params.length == 0) {
                res = dbc.executeQuery(sql);
            } else if (params.length == 1) {
                res = dbc.executeQuery(sql, params[0]);
            } else {
                res = dbc.executeQuery(sql, params);
            }

            // 对要填充的类，得到名称到字段的映射表
            HashMap<String, Field> nameToFieldMap = ClassUtil.getNameToFieldMap(clazz);

            // 对sql，检查是否所有列都能找到相应的字段
            int colNum = res.getMetaData().getColumnCount();
            Field[] fieldsOrderedByColumn = new Field[colNum];
            for (int i = 0; i < colNum; i++) {
                String colName = res.getMetaData().getColumnLabel(i + 1).toLowerCase();
                fieldsOrderedByColumn[i] = nameToFieldMap.get(colName);
            }

            ArrayList list = new ArrayList();
            int cnt = 0;
            while (res.next()) {
                if (cnt % 10000 == 0) {
                    logger.debug(cnt + " loaded");
                }
                cnt++;
                Object obj = clazz.newInstance();
                for (int i = 0; i < colNum; i++) {
                    Field field = fieldsOrderedByColumn[i];
                    if (field == null) continue;
                    final String fieldType = field.getType().getName();
                    if (fieldType.equals("java.lang.String")) {
                        field.set(obj, res.getString(i + 1));
                    } else if (fieldType.equals("int")) {
                        field.set(obj, res.getInt(i + 1));
                    } else if (fieldType.equals("double")) {
                        field.set(obj, res.getDouble(i + 1));
                    } else if (fieldType.equals("boolean")) {
                        field.set(obj, res.getBoolean(i + 1));
                    } else if (fieldType.equals("char")) {
                        final String s = res.getString(i + 1);
                        final char c;
                        if (s.length() == 0) {
                            c = ' ';
                        } else {
                            c = s.charAt(0);
                        }
                        field.set(obj, c);
                    } else {
                        field.set(obj, res.getObject(i + 1));
                    }
                }
                list.add(obj);
            }

            logger.debug(list.size() + " rows read.");

            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到对象
     * 根据sql中的列名，填充到实例的相应字段
     * 如果找不到字段，那么异常
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Object getObject(String sql, Class clazz, Object... params) throws Exception {
        logger.debug(sql);

        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            if (params.length == 0) {
                res = dbc.executeQuery(sql);
            } else if (params.length == 1) {
                res = dbc.executeQuery(sql, params[0]);
            } else {
                res = dbc.executeQuery(sql, params);
            }

            // 对要填充的类，得到名称到字段的映射表
            HashMap<String, Field> nameToFieldMap = ClassUtil.getNameToFieldMap(clazz);

            // 对sql，检查是否所有列都能找到相应的字段
            int colNum = res.getMetaData().getColumnCount();
            Field[] fieldsOrderedByColumn = new Field[colNum];
            for (int i = 0; i < colNum; i++) {
                String colName = res.getMetaData().getColumnLabel(i + 1).toLowerCase();
                fieldsOrderedByColumn[i] = nameToFieldMap.get(colName);
            }

            Object obj = clazz.newInstance();
            if (res.next()) {
                for (int i = 0; i < colNum; i++) {
                    Field field = fieldsOrderedByColumn[i];
                    if (field == null) continue;
                    final String fieldType = field.getType().getName();
                    if (fieldType.equals("java.lang.String")) {
                        field.set(obj, res.getString(i + 1));
                    } else if (fieldType.equals("int")) {
                        field.set(obj, res.getInt(i + 1));
                    } else if (fieldType.equals("double")) {
                        field.set(obj, res.getDouble(i + 1));
                    } else if (fieldType.equals("boolean")) {
                        field.set(obj, res.getBoolean(i + 1));
                    } else if (fieldType.equals("char")) {
                        final String s = res.getString(i + 1);
                        final char c;
                        if (s.length() == 0) {
                            c = ' ';
                        } else {
                            c = s.charAt(0);
                        }
                        field.set(obj, c);
                    } else {
                        field.set(obj, res.getObject(i + 1));
                    }
                }
                return obj;
            }

            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到值集合
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Set getValueSet(String sql, Object... params) throws Exception {
        return new HashSet(getValueList(sql, params));
    }


    /**
     * 得到值集合
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Set getIntegerSet(String sql, Object... params) throws Exception {
        return new HashSet(getIntegerList(sql, params));
    }


    /**
     * 得到值列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ArrayList getValueList(String sql, Object... params) throws Exception {
        // P.pstart("getListByIdCollection(" + sql + ", " + clazz + ")");
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            if (params.length == 0) {
                res = dbc.executeQuery(sql);
            } else if (params.length == 1) {
                res = dbc.executeQuery(sql, params[0]);
            } else {
                res = dbc.executeQuery(sql, params);
            }

            ArrayList list = new ArrayList();
            while (res.next()) {
                list.add(res.getObject(1));
            }

            // logger.debug(list.size() + " rows read.");

            // P.pfinish();
            return list;
        } catch (Exception e) {
            // P.pfinish();
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到值列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Integer> getIntegerList(String sql, Object... params) throws Exception {
        logger.debug("getIntList");
        logger.debug(sql);

        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            if (params.length == 0) {
                res = dbc.executeQuery(sql);
            } else if (params.length == 1) {
                res = dbc.executeQuery(sql, params[0]);
            } else {
                res = dbc.executeQuery(sql, params);
            }

            ArrayList list = new ArrayList();
            while (res.next()) {
                list.add(res.getInt(1));
            }

            logger.debug(list.size() + " rows read.");

            // P.pfinish();
            return list;
        } catch (Exception e) {
            // P.pfinish();
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到值列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<int[]> getIntegerArrayList(String sql, Object... params) throws Exception {
        logger.debug(sql);
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            if (params.length == 0) {
                res = dbc.executeQuery(sql);
            } else if (params.length == 1) {
                res = dbc.executeQuery(sql, params[0]);
            } else {
                res = dbc.executeQuery(sql, params);
            }

            int colNum = res.getMetaData().getColumnCount();
            List list = new ArrayList();
            while (res.next()) {
                int[] ints = new int[colNum];
                for (int i = 0; i < colNum; i++) {
                    ints[i] = res.getInt(i + 1);
                }
                list.add(ints);
            }

            logger.debug(list.size() + " rows read.");

            return list;
        } catch (Exception e) {
            // P.pfinish();
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到一个对象
     * 根据sql中的列名，填充到实例的相应字段
     * 如果找不到字段，那么异常
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Object get(String sql, Class clazz) throws Exception {
        P.pstart("get(" + sql + ", " + clazz + ")");
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql);

            // 对要填充的类，得到名称到字段的映射表
            HashMap<String, Field> nameToFieldMap = ClassUtil.getNameToFieldMap(clazz);

            // 对sql，检查是否所有列都能找到相应的字段
            int colNum = res.getMetaData().getColumnCount();
            Field[] fieldsOrderedByColumn = new Field[colNum];
            for (int i = 0; i < colNum; i++) {
                String colName = res.getMetaData().getColumnName(i + 1).toLowerCase();
                fieldsOrderedByColumn[i] = nameToFieldMap.get(colName);
            }

            Object obj = null;
            if (res.next()) {
                obj = clazz.newInstance();
                for (int i = 0; i < colNum; i++) {
                    Field field = fieldsOrderedByColumn[i];
                    if (field == null) continue;
                    final String fieldType = field.getType().getName();
                    if (fieldType.equals("java.lang.String")) {
                        field.set(obj, res.getString(i + 1));
                    } else if (fieldType.equals("int")) {
                        field.set(obj, res.getInt(i + 1));
                    } else if (fieldType.equals("double")) {
                        field.set(obj, res.getDouble(i + 1));
                    } else if (fieldType.equals("boolean")) {
                        field.set(obj, res.getBoolean(i + 1));
                    } else {
                        field.set(obj, res.getObject(i + 1));
                    }
                }
            }

            P.pfinish();
            return obj;
        } catch (Exception e) {
            P.pfinish();
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 得到集合
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashSet<String> getHashSet(String sql) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
//            logger.debug(sql);
            res = dbc.executeQuery(sql);

            HashSet<String> set = new HashSet<String>();
            while (res.next()) {
                String s = res.getString(1);
                if (s != null) {
                    s = s.trim();
                }
                set.add(s);
            }

            return set;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 得到集合
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashSet<Integer> getIntHashSet(String sql) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
//            logger.debug(sql);
            res = dbc.executeQuery(sql);

            HashSet<Integer> set = new HashSet<Integer>();
            while (res.next()) {
                int s = res.getInt(1);
                set.add(s);
            }

            return set;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    public static Vector<Integer> getVectorInt(String sql) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
//                logger.debug(sql);
            res = dbc.executeQuery(sql);

            Vector<Integer> set = new Vector<Integer>();
            while (res.next()) {
                int i = res.getInt(1);

                set.add(i);
            }

            return set;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到集合
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashSet<String> getHashSet(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            logger.debug(sql);
            res = dbc.executeQuery(sql, params);

            HashSet<String> set = new HashSet<String>();
            while (res.next()) {
                String s = res.getString(1);
                if (s != null) {
                    s = s.trim();
                }
                set.add(s);
            }

            return set;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到字符串列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Set<String> getStringSet(String sql, Object... params) throws SQLException {
        return new HashSet(getStringList(sql, params));
    }


    /**
     * 得到字符串列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ArrayList<String> getStringList(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            logger.debug(sql);
            res = dbc.executeQuery(sql, params);

            ArrayList<String> list = new ArrayList<String>();
            int i = 0;
            while (res.next()) {
                i++;
                if (i % 10000 == 0) {
                    logger.debug(i + " loaded");
                }
                String s = res.getString(1);
                if (s != null) {
                    s = s.trim();
                }
                list.add(s);
            }

            logger.debug(i + " loaded");
            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到字符串
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static String getString(String sql, String... params) throws SQLException {
        logger.debug(sql);

        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            if (res.next()) {
                String s = res.getString(1);
                if (s != null) {
                    s = s.trim();
                }
                return s;
            }

            return null;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 得到整数
     * 如果返回Integer.MIN_VALUE，表示没有查询到结果
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int getInt(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            if (res.next()) {
                return res.getInt(1);
            }

            return Integer.MIN_VALUE;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到整数
     * 如果返回Integer.MIN_VALUE，表示没有查询到结果
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int getIntSafely(String sql, int returnValueIfNull, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            if (res.next()) {
                return res.getInt(1);
            }

            return returnValueIfNull;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 得到列名列表和数据列表
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ArrayList[] getNameAndDataList(String sql) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql);

            int columnNum = res.getMetaData().getColumnCount();

            // construct column name list
            ArrayList<String> columnNameList = new ArrayList<String>();
            for (int i = 0; i < columnNum; i++) {
                columnNameList.add(res.getMetaData().getColumnName(i + 1));
            }

            // construct data list
            ArrayList<String[]> dataList = new ArrayList<String[]>();
            int cnt = 0;
            while (res.next()) {
                cnt++;
                if (cnt % 10000 == 0) {
                    logger.debug(cnt + " rows loaded.");
                }
                String[] record = new String[columnNum];
                for (int i = 0; i < columnNum; i++) {
                    record[i] = res.getString(i + 1);
                }
                dataList.add(record);
            }

            return new ArrayList[]{columnNameList, dataList};
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 得到列表，每一行记录为列表的一个元素，每条记录的多个字段记录为字符串数组
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<String[]> getStringsList(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            logger.debug(sql);
            res = dbc.executeQuery(sql, params);

            int columnNum = res.getMetaData().getColumnCount();

            // construct data list
            ArrayList<String[]> dataList = new ArrayList<String[]>();
            int cnt = 0;
            while (res.next()) {
                cnt++;
                if (cnt % 10000 == 0) {
                    logger.debug(cnt + " rows loaded.");
                }
                String[] record = new String[columnNum];
                for (int i = 0; i < columnNum; i++) {
                    record[i] = res.getString(i + 1);
                }
                dataList.add(record);
            }

            return dataList;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到表的记录数
     *
     * @return
     */
    public static int countTable(String tableName) throws SQLException {
        return countTable(tableName, null);
    }


    /**
     * 得到表的记录数
     *
     * @return
     */
    public static int countTable(String tableName, String condition) throws SQLException {
        String sql = "select count(*) from " + tableName;
        if (condition != null) {
            sql += " where " + condition;
        }
        return DBUtil.getInt(sql);
    }


    /**
     * 得到select语句中所选字段的个数
     *
     * @param sql
     * @return
     */
    public static int getFieldCount(String sql) {
        sql = sql.toLowerCase();
        int idx_begin = sql.indexOf("select");
        int idx_end = sql.indexOf("from");

        String fields = sql.substring(idx_begin + 6, idx_end);
        return StringUtil.count(fields, ',') + 1;
    }


    /**
     * 得到Map，其中查询的第一个字段为键，第二个字段为值
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashMap<String, String> getMap(String sql) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql);

            HashMap<String, String> map = new HashMap<String, String>();
            while (res.next()) {
                map.put(res.getString(1), res.getString(2));
            }

            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }



    /**
     * 得到Map，其中查询的第一个字段为键，第二个字段为值
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashMap<Integer, Integer> getIntToIntMap(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        logger.debug(sql);

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            while (res.next()) {
                map.put(res.getInt(1), res.getInt(2));
            }

            logger.debug(map.size() + " rows loaded");
            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到Map，其中查询的第一个字段为键，第二个字段为值
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashMap<String, Integer> getStringToIntMap(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            logger.debug(sql);
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            HashMap<String, Integer> map = new HashMap<String, Integer>();
            int count = 0;
            while (res.next()) {
                String string = res.getString(1);
                int anInt = res.getInt(2);
                map.put(string, anInt);

                count++;
                if(count%100000==0) {
                    System.out.println(count + " loaded");
                }
            }

            System.out.println(count + " loaded");

            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到Map，其中查询的第一个字段为键，第二个字段为值
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static HashMap<Integer, String> getIntToStringMap(String sql, Object... params) throws SQLException {
        ResultSet res = null;
        DBConnect dbc = null;

        try {
            logger.debug(sql);
            dbc = new DBConnect();
            res = dbc.executeQuery(sql, params);

            int count = 0;
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            while (res.next()) {
                count++;
                if(count % 100000 == 0) {
                    System.out.println("loading "+count);
                }
                map.put(res.getInt(1), res.getString(2));
            }

            System.out.println(count+"loaded");

            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    /**
     * 批量插入
     */
    public static void batchInsert(String tableName, String[] fieldNames, ArrayList<Object[]> fieldValueList) throws SQLException {
        P.pstart("batchInsert ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        final int fieldNum = fieldNames.length;

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        sb.append(fieldNames[0]);
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ").append(fieldNames[i]);
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ?");
        }
        sb.append(")");

        PreparedStatement preStatement = dbc.prepareStatement(sb.toString());

        for (Object[] values : fieldValueList) {
            for (int i = 0; i < fieldNum; i++) {
                //设置参数值
                DBConnect.setParameter(preStatement, i + 1, values[i]);
            }
            int ret = preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(fieldValueList.size() + " row(s) committed.");

        P.pfinish();
    }


    /**
     * 得到插入的sql
     *
     * @param tableName
     * @param fieldNames
     * @return
     */
    public static String getInsertSql(String tableName, String[] fieldNames) {
        final int fieldNum = fieldNames.length;

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        sb.append(fieldNames[0]);
        //sb.append('`').append(fieldNames[0]).append('`');
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ").append(fieldNames[i]);
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ?");
        }
        sb.append(")");

        return sb.toString();
    }


    /**
     * 得到创建表的sql
     *
     * @param clazz
     * @param tableName
     * @return
     */
    public static String getCreateTableSql(Class clazz, String tableName) {
        Field[] fields = getSimpleFields(clazz);
        final int fieldNum = fields.length;

        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(tableName).append(" (");
        sb.append("\n").append(fields[0].getName()).append('\t').append(getColumnType(fields[0]));
        for (int i = 1; i < fieldNum; i++) {
            sb.append(",\n").append(fields[i].getName()).append('\t').append(getColumnType(fields[i]));
        }
        sb.append(")");

        return sb.toString();
    }


    /**
     * 得到创建表的sql
     *
     * @param clazz
     * @param tableName
     * @param fieldNames
     * @return
     * @throws NoSuchFieldException
     */
    public static String getCreateTableSql(Class clazz, String tableName, String... fieldNames) throws NoSuchFieldException {
        final int fieldNum = fieldNames.length;

        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(tableName).append(" (");
        sb.append("\n").append(fieldNames[0]).append('\t').append(getColumnType(clazz.getField(fieldNames[0])));
        for (int i = 1; i < fieldNum; i++) {
            sb.append(",\n").append(fieldNames[i]).append('\t').append(getColumnType(clazz.getField(fieldNames[i])));
        }
        sb.append(")");

        return sb.toString();
    }


    /**
     * 从类名得到表名
     *
     * @param clazz
     * @return
     */
    private static String getTableName(Class clazz) {
        return clazz.getSimpleName() + "_" + TimeUtil.getDate_Time();
    }


    /**
     * 根据字段类型得到列类型
     *
     * @param field
     * @return
     */
    public static String getColumnType(Field field) {
        final String fieldType = field.getType().getName();
        if (fieldType.equals("int")) {
            return "int(11)";
        } else if (fieldType.equals("double")) {
            return "float";
        } else {
            return "varchar(1000)";
        }
    }


    /**
     * 创建表并插入
     *
     * @param objectList
     * @throws Exception
     */
    public static void createTableAndInsert(List objectList) throws Exception {
        logger.debug("Create table:");
        final Class clazz = objectList.get(0).getClass();
        String tableName = getTableName(clazz);
        String sql = getCreateTableSql(clazz, tableName);
        executeUpdate(sql);

        batchInsert(tableName, objectList);
    }


    /**
     * 创建表并插入
     *
     * @param objectList
     * @param filedNames
     * @throws Exception
     */
    public static void createTableAndInsert(ArrayList objectList, String... filedNames) throws Exception {
        logger.debug("Create table:");
        final Class clazz = objectList.get(0).getClass();
        String tableName = getTableName(clazz);
        String sql = getCreateTableSql(clazz, tableName, filedNames);
        executeUpdate(sql);

        batchInsert(tableName, objectList, filedNames);
    }

    /**
     * 批量插入
     */
    public static void batchInsert(String tableName, List objectList) throws Exception {
        if (objectList == null || objectList.size() == 0) {
            return;
        }

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        dbc = new DBConnect();
        dbc.setAutoCommit(false);

        Object firstObj = objectList.get(0);
        Field[] objectFields = getSimpleFields(firstObj.getClass());

        Set<String> tableFields = getTableFields(tableName);
        objectFields = filter(objectFields, tableFields);

        String[] fieldNames = ClassUtil.getFieldNames(objectFields);

        final String s = getInsertSql(tableName, fieldNames);
        logger.debug(s);
        preStatement = dbc.prepareStatement(s);

        final int fieldNum = fieldNames.length;

        logger.debug("batchInsert ...");

        Object obj = null;
        int i_obj = 0;
        try {
            for (i_obj = 0; i_obj < objectList.size(); i_obj++) {
                if(i_obj%10000==0) {
                    System.out.println("insert "+i_obj+"/"+objectList.size());
                }
                obj = objectList.get(i_obj);
                for (int i = 0; i < fieldNum; i++) {
                    //设置参数值
                    DBConnect.setParameter(preStatement, i + 1, objectFields[i].get(obj));
                }
                preStatement.executeUpdate();
            }

            logger.debug("Commit ...");
            dbc.commit();
            logger.debug(objectList.size() + " records committed.");
        } catch (Exception e) {
            if (obj != null) {
                logger.debug("object:");
                for (int i = 0; i < fieldNum; i++) {
                    logger.debug(objectFields[i].getName() + "\t" + objectFields[i].get(obj));
                }
            }
            throw new Exception("Exception at " + i_obj + "\n" + e.toString());
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }

    public static Set<String> getTableFields(String tableName) throws SQLException {
        String dbName;
        int idxOfDot = tableName.indexOf('.');
        if(idxOfDot<0) {
            // dot not found
            dbName = DBCPManager.getDBName();
        } else {
            dbName = tableName.substring(0, idxOfDot);
            tableName = tableName.substring(idxOfDot+1);
        }

        return getHashSet("SELECT LOWER(column_name)\n" +
                "FROM information_schema.columns\n" +
                "WHERE table_schema='"+dbName+"'\n" +
                "AND table_name='"+tableName+"'"
        );
    }


    public static Field[] filter(Field[] objFields, Set<String> tableFields) {
        List<Field> resultList = new ArrayList<Field>();
        for (Field field : objFields) {
            if (tableFields.contains(field.getName().toLowerCase())) {
                resultList.add(field);
            }
        }

        return resultList.toArray(new Field[resultList.size()]);
    }

    /**
     * 插入
     */
    public static void insertARecord(String tableName, Object record) throws Exception {
        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        dbc = new DBConnect();

        Field[] fields = getSimpleFields(record.getClass());
        String[] fieldNames = ClassUtil.getFieldNames(fields);

        final String s = getInsertSql(tableName, fieldNames);
        logger.debug(s);
        preStatement = dbc.prepareStatement(s);

        final int fieldNum = fieldNames.length;

        P.pstart("insert");

        try {
            for (int i = 0; i < fieldNum; i++) {
                //设置参数值
                DBConnect.setParameter(preStatement, i + 1, fields[i].get(record));
            }
            preStatement.executeUpdate();
            P.p("OK");
        } catch (Exception e) {
            P.p("Exception");
            logger.debug("object:");
            for (int i = 0; i < fieldNum; i++) {
                logger.debug(fields[i].getName() + "\t" + fields[i].get(record));
            }
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
        P.pfinish();
    }


    /**
     * 插入
     */
    public static void insert(String tableName, Object obj, String... fieldNames) throws Exception {
        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        dbc = new DBConnect();

        final String s = getInsertSql(tableName, fieldNames);
        logger.debug(s);
        preStatement = dbc.prepareStatement(s);

        final int fieldNum = fieldNames.length;

        P.pstart("insert");

        Class clazz = obj.getClass();
        try {
            for (int i = 0; i < fieldNum; i++) {
                //设置参数值
                DBConnect.setParameter(preStatement, i + 1, clazz.getField(fieldNames[i]).get(obj));
            }
            preStatement.executeUpdate();
            P.p("OK");
        } catch (Exception e) {
            P.p("Exception");
            logger.debug("object:");
            for (int i = 0; i < fieldNum; i++) {
                logger.debug(fieldNames[i] + "\t" + clazz.getField(fieldNames[i]).get(obj));
            }
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
        P.pfinish();
    }


    /**
     * 批量插入
     */
    public static void batchInsert(String tableName, List objectList, String... fieldNames) throws Exception, IllegalAccessException {
        if (objectList == null || objectList.size() == 0) {
            return;
        }

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        dbc = new DBConnect();
        dbc.setAutoCommit(false);

        Object firstObj = objectList.get(0);
        Class clazz = firstObj.getClass();

        final String s = getInsertSql(tableName, fieldNames);
        logger.debug(s);
        preStatement = dbc.prepareStatement(s);

        final int fieldNum = fieldNames.length;

        logger.debug("batchInsert ...");

        Object obj = null;
        int i_obj = 0;
        try {
            for (i_obj = 0; i_obj < objectList.size(); i_obj++) {
                if (i_obj % 10000 == 0) {
                    logger.debug(i_obj + " rows inserted.");
                }
                obj = objectList.get(i_obj);
                for (int i = 0; i < fieldNum; i++) {
                    //设置参数值
                    final Object fieldValueObj = clazz.getField(fieldNames[i]).get(obj);
                    final Object fieldValue;
                    if (fieldValueObj == null) {
                        fieldValue = null;
                    } else if (fieldValueObj instanceof Character) {
                        fieldValue = String.valueOf(((Character) fieldValueObj).charValue());
                    } else {
                        fieldValue = fieldValueObj;
                    }
                    DBConnect.setParameter(preStatement, i + 1, fieldValue);
                }
                preStatement.executeUpdate();
            }

            logger.debug("Commit ...");
            dbc.commit();
            logger.debug(objectList.size() + " records committed.");
        } catch (Exception e) {
            if (obj != null) {
                logger.debug("object:");
                for (int i = 0; i < fieldNum; i++) {
                    logger.debug(fieldNames[i] + "\t" + clazz.getField(fieldNames[i]).get(obj));
                }
            }
            throw new Exception("Exception at " + i_obj + "\n" + e.toString());
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 得到简单成员变量
     * 1. 非静态
     * 2. 简单类型: int, float, double, String
     *
     * @param clazz
     * @return
     */
    private static Field[] getSimpleFields(Class clazz) {
        final Field[] fields = clazz.getFields();

        ArrayList<Field> fieldList = new ArrayList<Field>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String type = field.getType().getSimpleName();
                if ("int".equals(type)
                        || "long".equals(type)
                        || "float".equals(type)
                        || "double".equals(type)
                        || "char".equals(type)
                        || "String".equals(type)
                        )
                    fieldList.add(field);
            }
        }

        Field[] resultFields = new Field[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            resultFields[i] = fieldList.get(i);
        }

        return resultFields;
    }


    /**
     * 批量插入
     */
    public static void batchInsert(String tableName, String keyFieldName, String countFieldName, CountTable ct) throws SQLException {
        logger.debug("batchInsert ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        sb.append(keyFieldName).append(',').append(countFieldName);
        sb.append(") VALUES(?,?)");

        PreparedStatement preStatement = dbc.prepareStatement(sb.toString());

        //设置参数值
        for (Map.Entry entry : ct.entrySet()) {
            DBConnect.setParameter(preStatement, 1, entry.getKey());
            DBConnect.setParameter(preStatement, 2, ((Int) entry.getValue()).i);
            preStatement.executeUpdate();
        }

        dbc.commit();
    }


    /**
     * 批量插入
     */
    public static void batchInsertMap(String tableName, String[] fieldNames, ArrayList<HashMap<String, Object>> fieldValueMapList) throws SQLException {
        logger.debug("batchInsert ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        final int fieldNum = fieldNames.length;

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        sb.append(fieldNames[0]);
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ").append(fieldNames[i]);
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i = 1; i < fieldNum; i++) {
            sb.append(", ?");
        }
        sb.append(")");

        PreparedStatement preStatement = dbc.prepareStatement(sb.toString());

        for (HashMap<String, Object> fieldValueMap : fieldValueMapList) {
            // logger.debug(fieldValueMap.get("annoToken").toString().length());
            for (int i = 0; i < fieldNum; i++) {
                //设置参数值
                DBConnect.setParameter(preStatement, i + 1, fieldValueMap.get(fieldNames[i]));
            }
            int ret = preStatement.executeUpdate();
        }

        dbc.commit();
    }


    /**
     * 按照“键”查找记录，更新“值”
     */
    public static void batchUpdateByKeyValueMap(String tableName, String keyFieldName, String valueFieldName, Map map) throws Exception {
        logger.debug("batchUpdateByKeyValueMap ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        StringBuffer sb = new StringBuffer();
        sb
                .append("UPDATE ").append(tableName).append(" SET ")
                .append(valueFieldName).append("=? WHERE ")
                .append(keyFieldName).append("=?");
        final String sql = sb.toString();
        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        final Iterator iterator = map.entrySet().iterator();
        final int size = map.size();
        Map.Entry entry;
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            entry = (Map.Entry) iterator.next();
            DBConnect.setParameter(preStatement, 1, entry.getValue());
            DBConnect.setParameter(preStatement, 2, entry.getKey());
            preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }


    /**
     * 批量修改
     */
    public static void batchUpdate(String tableName, List objList, String keyFieldName, String... valueFieldNames) throws Exception {
        if (objList == null || objList.size() == 0) {
            return;
        }

        logger.debug("batchUpdate ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(tableName).append(" SET ");

        int nValue = valueFieldNames.length;
        for (int i = 0; i < nValue - 1; i++) {
            sb.append(valueFieldNames[i]).append("=?, ");
        }
        sb.append(valueFieldNames[nValue - 1]).append("=? ");

        sb.append(" WHERE ").append(keyFieldName).append("=?");
        final String sql = sb.toString();
        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        Class clazz = objList.get(0).getClass();
        Field keyField = clazz.getField(keyFieldName);
        Field[] valueFields = new Field[valueFieldNames.length];
        for (int i = 0; i < valueFieldNames.length; i++) {
            String valueFieldName = valueFieldNames[i];
            valueFields[i] = clazz.getField(valueFieldName);
        }
        int size = objList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            Object obj = objList.get(i);
            for (int i_para = 0; i_para < valueFields.length; i_para++) {
                DBConnect.setParameter(preStatement, i_para + 1, valueFields[i_para].get(obj));
            }
            DBConnect.setParameter(preStatement, valueFields.length + 1, keyField.get(obj));
            preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }


    /**
     * 批量修改
     */
    public static void batchUpdate(String sql, List<Vector> valueVectorList) throws Exception {
        logger.debug("batchUpdate ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        final int size = valueVectorList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            Vector<String> vector = valueVectorList.get(i);
            // logger.debug("----------------" + vector.get(0));
            try {
                for (int j = 0; j < vector.size(); j++) {
                    DBConnect.setParameter(preStatement, j + 1, vector.get(j));
                }
                preStatement.executeUpdate();
            } catch (Exception e) {
                logger.debug(vector.get(1));
                logger.debug(vector.get(2));
                logger.debug(vector.get(3));
                logger.debug(vector.get(4));
                logger.debug(vector.get(5));
                logger.debug("year[" + vector.get(6) + "]");
                e.printStackTrace();
                throw e;
            }
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }


    /**
     * 批量修改
     */
    public static void batchUpdateWithValueList(String sql, List valueList) throws Exception {
        logger.debug("batchUpdateWithValueList ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        final int size = valueList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            try {
                DBConnect.setParameter(preStatement, 1, valueList.get(i));
                preStatement.executeUpdate();
            } catch (Exception e) {
                logger.debug(valueList.get(i));
                e.printStackTrace();
                throw e;
            }
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }

    /**
     * 批量修改
     */
    public static void batchUpdate(String tableName, String keyFieldName, String valueFieldName, ArrayList<String> keyList, ArrayList<String> valueList) throws Exception {
        logger.debug("batchUpdate ...");

        if (keyList.size() != valueList.size()) {
            throw new Exception("keyList.size(" + keyList.size() + ")!=valueList.size(" + valueList.size() + ")");
        }

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        StringBuffer sb = new StringBuffer();
        sb
                .append("UPDATE ").append(tableName).append(" SET ")
                .append(valueFieldName).append("=? WHERE ")
                .append(keyFieldName).append("=?");
        final String sql = sb.toString();
        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        final int size = keyList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            DBConnect.setParameter(preStatement, 1, valueList.get(i));
            DBConnect.setParameter(preStatement, 2, keyList.get(i));
            preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }

    /**
     * 批量修改
     */
    public static void batchUpdate(String tableName, String keyFieldName, String valueFieldName, ArrayList<String[]> keyValueList) throws Exception {
        logger.debug("batchUpdate ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        StringBuffer sb = new StringBuffer();
        sb
                .append("UPDATE ").append(tableName).append(" SET ")
                .append(valueFieldName).append("=? WHERE ")
                .append(keyFieldName).append("=?");
        final String sql = sb.toString();
        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        final int size = keyValueList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            DBConnect.setParameter(preStatement, 1, keyValueList.get(i)[1]);
            DBConnect.setParameter(preStatement, 2, keyValueList.get(i)[0]);
            preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }


    /**
     * 批量修改
     */
    public static void batchUpdate(String tableName, String keyFieldName, String[] valueFieldNames, ArrayList objectList) throws Exception {
        logger.debug("batchUpdate ...");

        if (objectList == null || objectList.size() == 0) {
            return;
        }

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        try {
            dbc = new DBConnect();
            dbc.setAutoCommit(false);

            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE ").append(tableName).append(" SET ");
            sb.append(valueFieldNames[0]).append("=?");
            final int length_valueFields = valueFieldNames.length;
            for (int i = 1; i < length_valueFields; i++) {
                sb.append(',').append(valueFieldNames[i]).append("=?");
            }
            sb.append(" WHERE ").append(keyFieldName).append("=?");

            final String sql = sb.toString();
            logger.debug(sql);

            preStatement = dbc.prepareStatement(sql);

            Class clazz = objectList.get(0).getClass();

            //设置参数值
            final int size = objectList.size();
            for (int i = 0; i < size; i++) {
                if ((i + 1) % 10000 == 0) {
                    logger.debug("processing " + (i + 1) + "/" + size);
                }
                final Object obj = objectList.get(i);
                for (int i_value = 0; i_value < length_valueFields; i_value++) {
                    DBConnect.setParameter(preStatement, i_value + 1, clazz.getField(valueFieldNames[i_value]).get(obj));
                }
                DBConnect.setParameter(preStatement, length_valueFields + 1, clazz.getField(keyFieldName).get(obj));
                preStatement.executeUpdate();
            }

            dbc.commit();
            logger.debug(size + " rows committed.");
        } catch (Exception e) {
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 修改
     */
    public static void update(String tableName, String keyFieldName, String[] valueFieldNames, Object object) throws Exception {
        logger.debug("update ...");

        if (object == null) {
            return;
        }

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        try {
            dbc = new DBConnect();

            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE ").append(tableName).append(" SET ");
            sb.append(valueFieldNames[0]).append("=?");
            final int length_valueFields = valueFieldNames.length;
            for (int i = 1; i < length_valueFields; i++) {
                sb.append(',').append(valueFieldNames[i]).append("=?");
            }
            sb.append(" WHERE ").append(keyFieldName).append("=?");

            final String sql = sb.toString();
            logger.debug(sql);

            preStatement = dbc.prepareStatement(sql);

            Class clazz = object.getClass();

            //设置参数值
            for (int i_value = 0; i_value < length_valueFields; i_value++) {
                DBConnect.setParameter(preStatement, i_value + 1, clazz.getField(valueFieldNames[i_value]).get(object));
            }
            DBConnect.setParameter(preStatement, length_valueFields + 1, clazz.getField(keyFieldName).get(object));
            preStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 删除表中所有内容
     *
     * @param tableName
     * @throws SQLException
     */
    public static void truncate(String tableName) throws SQLException {
        logger.debug("truncate table: " + tableName);

        DBConnect dbc = new DBConnect();

        final String sql = "DELETE FROM " + tableName;
        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);
        preStatement.executeUpdate();
    }


    /**
     * 执行DML语句
     *
     * @param sql
     * @throws SQLException
     */
    public static void executeUpdate(String sql) throws SQLException {
        logger.debug("execute:\n" + sql);

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        try {
            dbc = new DBConnect();

            preStatement = dbc.prepareStatement(sql);
            int n = preStatement.executeUpdate();

            logger.debug(n + " rows affected.");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 执行DML语句
     *
     * @param sql
     * @throws SQLException
     */
    public static void executeUpdate(String sql, Object... param) throws SQLException {
        logger.debug("execute:\n" + sql);

        DBConnect dbc = null;
        PreparedStatement preStatement = null;

        try {
            dbc = new DBConnect();

            preStatement = dbc.prepareStatement(sql);
            for (int i = 0; i < param.length; i++) {
                DBConnect.setParameter(preStatement, i + 1, param[i]);
            }
            int affectedRows = preStatement.executeUpdate();
            logger.debug(affectedRows + " rows affected.");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preStatement != null) {
                try {
                    preStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preStatement = null;
            }
            if (dbc != null) {
                dbc.close();
            }
        }

    }


    /**
     * 得到列名
     *
     * @param res
     * @return
     * @throws SQLException
     */
    public static String[] getColumnNames(ResultSet res) throws SQLException {
        ResultSetMetaData rsm = res.getMetaData();
        int colNum = rsm.getColumnCount();
        String[] names = new String[colNum];

        for (int i = 0; i < colNum; i++) {
            names[i] = rsm.getColumnName(i + 1);
        }

        return names;
    }


    /**
     * 一条记录的所有字段都放在<字段名, 字段值>的Map中
     * 字段名为字符串，字段值为字符串
     *
     * @param sql
     * @return
     */
    public static ArrayList<HashMap<String, String>> getFieldMapList(String sql) throws SQLException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        ResultSet res = null;
        DBConnect dbc = null;

        try {
            dbc = new DBConnect();
            res = dbc.executeQuery(sql);

            String[] colNames = getColumnNames(res);

            while (res.next()) {
                HashMap<String, String> fieldMap = new HashMap<String, String>();
                for (String colName : colNames) {
                    fieldMap.put(colName, res.getString(colName));
                }
                list.add(fieldMap);
            }

            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dbc != null) {
                dbc.close();
            }
        }
    }


    /**
     * 批量修改
     * sql中报含一些参数（？）
     * 值为对象的字段值
     */
    public static void batchExecute(String sql, List objList, String... valueFieldNames) throws Exception {
        logger.debug("batchUpdate ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        logger.debug(sql);

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        //设置参数值
        Class clazz = objList.get(0).getClass();
        Field[] valueFields = new Field[valueFieldNames.length];
        for (int i = 0; i < valueFieldNames.length; i++) {
            String valueFieldName = valueFieldNames[i];
            valueFields[i] = clazz.getField(valueFieldName);
        }
        int size = objList.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            Object obj = objList.get(i);
            for (int i_para = 0; i_para < valueFields.length; i_para++) {
                DBConnect.setParameter(preStatement, i_para + 1, valueFields[i_para].get(obj));
            }
            preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(size + " rows committed.");
    }


    /**
     * 批量修改
     * sql中报含一些参数（？）
     * 值为对象的字段值
     */
    public static int batchExecuteWithValueCollection(String sql, Collection valueCollection) throws Exception {
        logger.debug("batchUpdate ...");

        DBConnect dbc = new DBConnect();
        dbc.setAutoCommit(false);

        logger.debug(sql);

        int nrAffectedRows = 0;

        PreparedStatement preStatement = dbc.prepareStatement(sql);

        int size = valueCollection.size();
        final Iterator iterator = valueCollection.iterator();
        for (int i = 0; i < size; i++) {
            Object obj = iterator.next();
            if (obj == null) {
                break;
            }

            if ((i + 1) % 10000 == 0) {
                logger.debug("processing " + (i + 1) + "/" + size);
            }
            DBConnect.setParameter(preStatement, 1, obj);
            nrAffectedRows += preStatement.executeUpdate();
        }

        dbc.commit();
        logger.debug(nrAffectedRows + " rows committed.");

        return nrAffectedRows;
    }


    public static String substituteParameter(String sql, Object... params) {
        if (params == null || params.length == 0) {
            return sql;
        }

        String[] pieces = sql.split("\\?");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pieces.length - 1; i++) {
            sb.append(pieces[i]).append(getParamValue(params[i]));
        }
        sb.append(pieces[pieces.length - 1]);

        return sb.toString();
    }


    public static String getParamValue(Object param) {
        if (param == null) {
            return "NULL";
        } else if (param instanceof String) {
            return "'" + ((String) param).replace("'", "\\'") + "'";
        } else {
            String type = param.getClass().getName();
            if (type.equals("int")
                    || type.equals("double")
                    || type.equals("boolean")
                    ) {
                return param.toString();
            } else if (type.equals("char")) {
                return "'" + param + "'";
            } else {
                return "'" + param + "'";
            }
        }
    }



    public static void setFieldValue(ResultSet res, int idxInRes, Object obj, Field field) throws IllegalAccessException, SQLException {
        final String fieldType = field.getType().getName();
        if (fieldType.equals("java.lang.String")) {
            field.set(obj, res.getString(idxInRes));
        } else if (fieldType.equals("int")) {
            field.set(obj, res.getInt(idxInRes));
        } else if (fieldType.equals("double")) {
            field.set(obj, res.getDouble(idxInRes));
        } else if (fieldType.equals("boolean")) {
            field.set(obj, res.getBoolean(idxInRes));
        } else if (fieldType.equals("char")) {
            final String s = res.getString(idxInRes);
            final char c;
            if (s == null || s.length() == 0) {
                c = ' ';
            } else {
                c = s.charAt(0);
            }
            field.set(obj, c);
        } else {
            field.set(obj, res.getObject(idxInRes));
        }
    }

    /**
     * 将ResultSet注入到Bean中
     * @param rs
     * @param obj
     * @throws Exception
     */
    public static void result2Bean(ResultSet rs, Object obj) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        int columncount = metaData.getColumnCount();
        for (int i = 1; i <= columncount; i++) {
            String fieldName = metaData.getColumnName(i);
            //fieldName = fieldName.toLowerCase();
            Field field = null;
            try {
                field = obj.getClass().getDeclaredField(fieldName);
            } catch (Exception e) {
                continue;
            }
            final String fieldType = field.getType().getName();
            Object fieldValue = null;
            if (fieldType.equals("java.lang.String")) {
                fieldValue = rs.getString(i);
            } else if (fieldType.equals("int")) {
                fieldValue = rs.getInt(i);
            } else if (fieldType.equals("double")) {
                fieldValue = rs.getDouble(i);
            } else if (fieldType.equals("boolean")) {
                fieldValue = rs.getBoolean(i);
            } else if (fieldType.equals("char")) {
                final String s = rs.getString(i);
                final char c;
                if (s == null || s.length() == 0) {
                    c = ' ';
                } else {
                    c = s.charAt(0);
                }
            } else {
                fieldValue = rs.getObject(i);
            }
            Method method = obj.getClass().getMethod("set"+StringUtil.upperInitial(fieldName), field.getType());
            method.invoke(obj, fieldValue);
        }
    }

    /**
     * 将变量转化为mysql对应的值
     * @param obj
     * @return
     */
    public static String convert(Object obj) {
        if (obj == null) {
            return "null";
        }
        String s = obj.toString();
        s = StringUtil.mysqlEscapeStr(s);
        s = "'"+s+"'";
        return s;
    }

}
