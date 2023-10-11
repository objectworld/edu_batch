package com.imvux21.batchprocessingdemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // enable scheduling
public class BatchProcessingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingDemoApplication.class, args);
    }

}
