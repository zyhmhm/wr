package com.wr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//eureka的注解
@EnableEurekaServer
@SpringBootApplication
public class WrEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrEurekaApplication.class, args);
	}

}
