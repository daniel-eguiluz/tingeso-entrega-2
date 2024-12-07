package com.tutorial.msevaluacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsEvaluacionApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsEvaluacionApplication.class, args);
	}
}
