package com.bd;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendBdApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackendBdApplication.class, args);
	}

	// En caso de requerir un log de inicio que indique algo agregar en este metodo
	@Override
	public void run(ApplicationArguments args) throws Exception {}

}
