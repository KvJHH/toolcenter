package top.superookie.toolcenter;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import top.superookie.toolcenter.database.DBInfo;
import top.superookie.toolcenter.database.DBOperate;
import top.superookie.toolcenter.exception.ToolException;
import top.superookie.toolcenter.http.HttpOperate;
import top.superookie.toolcenter.http.HttpUtils;

import java.sql.Connection;
import java.util.Map;

import static top.superookie.toolcenter.http.HttpUtils.doLogin;

public class ToolTest {

    private String token;

    private String host = ConfigProperty.get("orc.host");

//    @BeforeClass
    public void login() {

    }

    @Test
    public void test01() {
        Connection conn = DBInfo.getConn("db.orchestrator");
        Map<String, Object> map = DBOperate.selectOne(conn, "select count(*) as total from job;");
        System.out.println(map.get("total"));
    }

    @Test(description = "获取token")
    public void test02() {

    }

}
