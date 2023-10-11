package com.imvux21.batchprocessingdemo.listener;

import com.imvux21.batchprocessingdemo.entity.Customer;
import com.imvux21.batchprocessingdemo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            customerRepository.findAllByCountry("vietnam").forEach(customer -> log.info("Found <{{}}> in the database.", customer));
        }
    }
}
