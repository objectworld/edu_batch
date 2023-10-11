package com.imvux21.batchprocessingdemo.runner;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class JobRunner {
    private final static Logger log = LoggerFactory.getLogger(JobRunner.class);

    private final JobLauncher jobLauncher;

    private final Job importCustomerJob;

    private final Job exportCustomerJob;

    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    // run the job every 1 minute
    @Scheduled(cron = "0 * * * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void runJob() {
        // job parameters are used to pass data to the job
        // in this case, we pass the current time as a long
        // this is useful for logging purposes
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        // Check if the previous scheduled task is still running
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            log.warn("Previous job is still running. Skipping this execution.");
            return;
        }

        scheduledFuture = taskScheduler.schedule(() -> {
            try {
                // run the job
                // the job is executed asynchronously
                // the job will be executed by a thread from the thread pool
                // the thread pool is defined in the application.properties file

                // jobLauncher.run(importCustomerJob, jobParameters);
                jobLauncher.run(exportCustomerJob, jobParameters);
                log.info("Job finished");
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                log.error("Error running job", e);
            }
        }, new CronTrigger("0 * * * * *"));

    }

    // stop scheduling the job
    public void stopJob() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
