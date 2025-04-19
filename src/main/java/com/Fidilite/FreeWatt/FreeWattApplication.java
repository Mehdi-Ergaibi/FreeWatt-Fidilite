package com.Fidilite.FreeWatt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FreeWattApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreeWattApplication.class, args);
	}

	

}
