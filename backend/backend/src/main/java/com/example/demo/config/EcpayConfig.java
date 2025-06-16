package com.example.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter // 使用 Lombok 自動產生 getter 方法
public class EcpayConfig {

    @Value("${ecpay.logistics.url}")
    private String apiUrl;

    @Value("${ecpay.logistics.merchant-id}")
    private String merchantId;

    @Value("${ecpay.logistics.hash-key}")
    private String hashKey;

    @Value("${ecpay.logistics.hash-iv}")
    private String hashIv;
}