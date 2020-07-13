package com.wr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableEurekaClient
//@SpringBootApplication
@MapperScan("com.wr.dao")
@EnableFeignClients
@SpringCloudApplication
public class WrOrdermakingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrOrdermakingApplication.class, args);
	}

}
