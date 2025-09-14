package com.trenkwalder.parttimeemployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@Async
@SpringBootApplication
public class PartTimeEmploymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartTimeEmploymentApplication.class, args);
	}

}
