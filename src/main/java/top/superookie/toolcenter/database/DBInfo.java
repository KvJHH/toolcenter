package top.superookie.toolcenter.database;

import top.superookie.toolcenter.ConfigProperty;
import top.superookie.toolcenter.http.HttpUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBInfo {

    private static Map<String, Connection> connMap = new HashMap<>();

    public static Connection getConn(String dbName) {
        Connection conn;
        if ((conn = connMap.get(dbName)) == null) {
            try {
//                System.out.println("初始化db: " + dbName);
                String dbUrl = ConfigProperty.get(dbName);
                conn = DriverManager.getConnection(dbUrl);
                connMap.put(dbName, conn);
                return conn;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("从map直接取:" + dbName);
        return conn;
    }





}
