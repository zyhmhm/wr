package com.wr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.wr.dao")
@EnableEurekaClient
public class WrSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrSsoApplication.class, args);
	}

}
