package com.wr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.wr.dao")
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker
public class WrTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrTransferApplication.class, args);
	}

}
