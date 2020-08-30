package top.superookie.toolcenter;

import org.testng.annotations.Test;
import top.superookie.toolcenter.database.DBInfo;
import top.superookie.toolcenter.database.DBOperate;

import java.sql.Connection;
import java.util.Map;

public class ConfigPropertyTest {

    @Test
    public void test01() {
        Connection conn = DBInfo.getConn("db.orchestrator");
        Map<String, Object> map = DBOperate.selectOne(conn, "select count(*) as total from job;");
        System.out.println(map.get("total"));
    }

}
