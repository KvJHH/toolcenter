package top.superookie.toolcenter.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpOperate {

    public static CloseableHttpClient httpClient = HttpUtils.getHttpClientWithSSL();

    public static CallResult execute(HttpUriRequest request) {
        CallResult callResult = new CallResult();
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            callResult.setStatus(httpResponse.getStatusLine().getStatusCode());
            callResult.setMessage(httpResponse.getStatusLine().getReasonPhrase());
            callResult.setEntity(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return callResult;
    }

}
