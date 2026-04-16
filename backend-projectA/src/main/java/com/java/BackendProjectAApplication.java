package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BackendProjectAApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendProjectAApplication.class, args);
	}

}
