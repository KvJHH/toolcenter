package top.superookie.toolcenter.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOperate {

    public static List<Map<String, Object>> select(Connection conn, String sql) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                transferRsToMap(rs, map);
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, Object> selectOne(Connection conn, String sql) {
        List<Map<String, Object>> list = select(conn, sql);
        return list.size() > 0 ? list.get(0) : null;
    }

    public static int execute(Connection conn, String sql) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void transferRsToMap(ResultSet rs, Map<String, Object> map) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            map.put(metaData.getColumnName(i), rs.getObject(i));
        }
    }

}
