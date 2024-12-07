package com.tutorial.mssolicitud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsSolicitudApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsSolicitudApplication.class, args);
	}
}


