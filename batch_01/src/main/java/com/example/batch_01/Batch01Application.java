package com.example.batch_01;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableBatchProcessing 필수 아님
public class Batch01Application {
	public static void main(String[] args) {
		SpringApplication.run(Batch01Application.class, args);
	}
}
