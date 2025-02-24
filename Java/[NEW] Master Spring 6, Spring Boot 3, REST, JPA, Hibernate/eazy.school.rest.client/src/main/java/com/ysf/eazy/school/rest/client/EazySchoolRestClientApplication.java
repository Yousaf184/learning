package com.ysf.eazy.school.rest.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.ysf.eazy.school.rest.client.clients"})
public class EazySchoolRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EazySchoolRestClientApplication.class, args);
	}

}
