package com.wr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.wr.dao")
@SpringBootApplication
public class WrWarehousemanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrWarehousemanagementApplication.class, args);
	}

}
