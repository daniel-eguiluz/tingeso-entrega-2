package com.tutorial.msseguimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsSeguimientoApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsSeguimientoApplication.class, args);
	}
}
