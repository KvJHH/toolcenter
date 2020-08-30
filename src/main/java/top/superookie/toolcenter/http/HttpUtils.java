package top.superookie.toolcenter.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import top.superookie.toolcenter.ConfigProperty;
import top.superookie.toolcenter.exception.ErrorCode;
import top.superookie.toolcenter.exception.ToolException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;


public class HttpUtils {

    public static CloseableHttpClient getHttpClientWithSSL() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(factory).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void concurrentSend(int num, Runnable runnable) {
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < num; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        latch.countDown();
        executorService.shutdown();
    }

    public static <T> void execute(CountDownLatch latch, Supplier<T> supplier, List<T> collector) {
        try {
            latch.await();
            T t = supplier.get();
            collector.add(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> concurrentSend(int num, Supplier<T> supplier) {
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        CountDownLatch latch = new CountDownLatch(1);
        List<T> collector = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            executorService.submit(() -> execute(latch, supplier, collector));
        }
        latch.countDown();
        executorService.shutdown();
        return collector;
    }

    public static <T> List<T> concurrentSend(List<Supplier<T>> suppliers) {
        ExecutorService executorService = Executors.newFixedThreadPool(suppliers.size());
        CountDownLatch latch = new CountDownLatch(1);
        List<T> collector = new ArrayList<>();
        for (Supplier<T> supplier : suppliers) {
            executorService.submit(() -> execute(latch, supplier, collector));
        }
        latch.countDown();
        executorService.shutdown();
        return collector;
    }

    private static String doLogin() {
        String host = ConfigProperty.get("orc.host");
        String user = ConfigProperty.get("orc.user");
        String password = ConfigProperty.get("orc.password");
        HttpPost httpPost = new HttpPost(host + "/api/v1/authenticate");
        StringEntity stringEntity = new StringEntity(String.format("{\"username\":\"%s\",\"password\":\"%s\",\"captcha\":null}", user, password), "UTF-8");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        CallResult callResult = HttpOperate.execute(httpPost);
        if (callResult.getStatus() != 200) {
            throw new ToolException(callResult.getStatus(), callResult.getMessage());
        }
        System.out.println("default token: " + callResult);
        String token = JSON.parseObject(callResult.getEntity()).getString("id_token");
        if (StringUtils.isEmpty(token)) {
            throw new ToolException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return "Bearer " + token;
    }

    public static CallResult doLogin(String user, String password) {
        String host = ConfigProperty.get("orc.host");
        HttpPost httpPost = new HttpPost(host + "/api/v1/authenticate");
        StringEntity stringEntity = new StringEntity(String.format("{\"username\":\"%s\",\"password\":\"%s\",\"captcha\":null}", user, password), "UTF-8");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        CallResult callResult = HttpOperate.execute(httpPost);
        return callResult;
    }

    public static HttpPost orcHttpPost(String api, OrcHeader ...orcHeaders) {
        HttpPost httpPost = new HttpPost(ConfigProperty.get("orc.host") + api);
        httpPost.setHeader("Authorization", accessToken);
        for (OrcHeader orcHeader : orcHeaders) {
            httpPost.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpPost;
    }

    public static HttpGet orcHttpGet(String api, OrcHeader ...orcHeaders) {
        HttpGet httpGet = new HttpGet(ConfigProperty.get("orc.host") + api);
        httpGet.setHeader("Authorization", accessToken);
        for (OrcHeader orcHeader : orcHeaders) {
            httpGet.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpGet;
    }

    public static HttpPut orcHttpPut(String api, OrcHeader ...orcHeaders) {
        HttpPut httpPut = new HttpPut(ConfigProperty.get("orc.host") + api);
        httpPut.setHeader("Authorization", accessToken);
        for (OrcHeader orcHeader : orcHeaders) {
            httpPut.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpPut;
    }

    public static HttpDelete orcHttpDelete(String api, OrcHeader ...orcHeaders) {
        HttpDelete httpDelete = new HttpDelete(ConfigProperty.get("orc.host") + api);
        httpDelete.setHeader("Authorization", accessToken);
        for (OrcHeader orcHeader : orcHeaders) {
            httpDelete.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpDelete;
    }

    private static final String accessToken = doLogin();

    public static HttpPost actHttpPost(String api, OrcHeader ...orcHeaders) {
        HttpPost httpPost = new HttpPost(ConfigProperty.get("act.host") + api);
        for (OrcHeader orcHeader : orcHeaders) {
            httpPost.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpPost;
    }

    public static HttpGet actHttpGet(String api, OrcHeader ...orcHeaders) {
        HttpGet httpGet = new HttpGet(ConfigProperty.get("act.host") + api);
        for (OrcHeader orcHeader : orcHeaders) {
            httpGet.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpGet;
    }

    public static HttpPut actHttpPut(String api, OrcHeader ...orcHeaders) {
        HttpPut httpPut = new HttpPut(ConfigProperty.get("act.host") + api);
        for (OrcHeader orcHeader : orcHeaders) {
            httpPut.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpPut;
    }

    public static HttpDelete actHttpDelete(String api, OrcHeader ...orcHeaders) {
        HttpDelete httpDelete = new HttpDelete(ConfigProperty.get("act.host") + api);
        for (OrcHeader orcHeader : orcHeaders) {
            httpDelete.setHeader(orcHeader.getName(), orcHeader.getValue());
        }
        return httpDelete;
    }

}
