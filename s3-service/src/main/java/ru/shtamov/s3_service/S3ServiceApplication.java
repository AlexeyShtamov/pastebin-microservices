package ru.shtamov.s3_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class S3ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3ServiceApplication.class, args);
	}

}
