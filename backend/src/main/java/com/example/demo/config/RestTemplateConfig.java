package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.config.RequestConfig;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 创建HTTP客户端配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000) // 10秒连接超时
                .setSocketTimeout(30000)  // 30秒读取超时
                .build();
        
        // 创建HTTP客户端，支持gzip压缩
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        
        // 创建请求工厂
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        
        return new RestTemplate(factory);
    }
}
