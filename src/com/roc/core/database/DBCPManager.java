package com.roc.core.database;

import com.roc.core.Utils.StringUtil;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * User: Lu Tingming
 * Date: 2010-11-12 12:42:59
 * Desc: 数据库连接连接池管理类
 * <p/>
 * <P>采用DBCP构建数据库连接池，构建数据库连接池有两种方法，还有一种方法参见：<CODE>DBConnectionPoolManager类中的实现</CODE></P>
 * <P>这种方法可以适用于服务器启动方式，也适用于非服务器启动方式</P>
 * 提供两种创建连接池的方法：
 * <ol>
 * <li> 默认的标准配置，根据默认的配置方式构建连接池
 * </li>
 * <li> 根据用户设置构建连接池，该方式需用户传入<em>Properties</em>参数，该参数中需包括：
 * <ol>
 * <li>  driverClassName：驱动名
 * </li>
 * <li>  username：数据库用户名
 * </li>
 * <li>  password：数据库密码
 * </li>
 * <li>  url：数据库连接URL
 * </li>
 * <li>  maxWait：最大等待时间
 * </li>
 * <li>  maxActive：最大连接数
 * </li>
 * <li>  initialSize：初始连接数
 * </li>
 * </ol>
 * </li>
 * </ol>
 * <p/>
 * <B>NOTE:该方法也可提供XML文件进行配置的方法，目前没有实现</B>
 */
public class DBCPManager {
    private static DataSource defaultDataSource;
    private static HashMap<String, DataSource> dataSourceMap;
    private static Properties defaultProperties;

    static {
        initDefaultDataSource();
        dataSourceMap = new HashMap<String, DataSource>();
    }


    /**
     * 初始化默认数据库连接
     */
    private static void initDefaultDataSource() {
        defaultProperties = getProperties("dbcpconfig.properties");
        defaultDataSource = getDataSource(defaultProperties);
    }


    /**
     * 获得数据源
     * @param propertiesFileName
     * @return
     */
    private static DataSource getDataSource(String propertiesFileName) {
        Properties properties = getProperties(propertiesFileName);
        return getDataSource(properties);
    }


    /**
     * 获得数据源
     * @param properties
     * @return
     */
    private static DataSource getDataSource(Properties properties) {
        DataSource dataSource;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            dataSource = null;
            e.printStackTrace();
        }

        return dataSource;
    }


    private static Properties getProperties(String propertiesFileName) {
        Properties properties = new Properties();
        //读取配置文件
        InputStream is = DBCPManager.class.getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return properties;
    }


    /**
     * 得到默认的连接
     * @return
     */
    public static Connection getDefaultConnection() {
        Connection conn = null;
        try {
            if (null == defaultDataSource) {
                initDefaultDataSource();
            }
            conn = defaultDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            System.out.println("CONM为NULL!");
        }
        return conn;
    }


    /**
     * 根据配置名称得到的连接
     * @return
     */
    public static Connection getConnection(String configName) {
        if(StringUtil.isEmpty(configName)) {
            return getDefaultConnection();
        }

        Connection conn = null;
        try {
            DataSource dataSource = dataSourceMap.get(configName);
            if (null == dataSource) {
                dataSource = getDataSource("dbcpconfig." + configName + ".properties");
                dataSourceMap.put(configName, dataSource);
            }
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            System.out.println("CONM为NULL!");
        }
        return conn;
    }


    /**
     * 得到数据库名
     * @return
     */
    public static String getDBName() {
        final String url = defaultProperties.getProperty("url");
        int begin = url.lastIndexOf('/');
        int end = url.indexOf('?');
        return url.substring(begin + 1, end);
    }
}

