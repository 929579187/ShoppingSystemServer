package com.sy.shopping.utils;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
    private static Properties properties;

    static {
        properties = PropertiesUtils.loadProperties("db.properties");
        DbUtils.loadDriver(properties.getProperty("driver"));
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("pwd"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
