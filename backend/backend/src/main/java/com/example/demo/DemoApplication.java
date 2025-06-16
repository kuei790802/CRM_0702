package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // 新增
import org.springframework.web.client.RestTemplate; // 新增

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean // 將 RestTemplate 交給 Spring 管理
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}