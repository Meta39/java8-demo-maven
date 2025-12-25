package com.fu.springbootssldemo.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public final class RestTemplateSkipSslUtils {

    private static volatile RestTemplate restTemplate;

    private RestTemplateSkipSslUtils() {
    }

    private static CloseableHttpClient skipSslHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建信任所有证书的TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 创建SSL上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // 创建SSL Socket Factory，跳过主机名验证
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE
        );

        // 注册http和https对应的socket工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslSocketFactory)
                .build();

        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        // 配置连接池参数
        connectionManager.setMaxTotal(200); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(50); // 每个路由最大连接数
        connectionManager.setValidateAfterInactivity(300_000); // 连接空闲时间（ms）

        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60_000)     // 连接超时时间（ms）
                .setSocketTimeout(60_000)      // 读取超时时间（ms）
                .setConnectionRequestTimeout(3_000) // 从连接池获取连接的超时时间（ms）
                .build();

        // 创建HttpClient
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    public static RestTemplate getInstance() {
        //双重检查锁定单例模式
        if (restTemplate == null) {
            synchronized (RestTemplateSkipSslUtils.class) {
                if (restTemplate == null) {
                    ClientHttpRequestFactory clientHttpRequestFactory;
                    try {
                        clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(skipSslHttpClient());
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        throw new RuntimeException(e);
                    }
                    restTemplate = new RestTemplate(clientHttpRequestFactory);
                }
            }
        }
        return restTemplate;
    }


}
