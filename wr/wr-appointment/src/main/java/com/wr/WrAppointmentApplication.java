package com.wr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@MapperScan("com.wr.dao")
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
public class WrAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrAppointmentApplication.class, args);
	}

}
