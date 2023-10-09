package com.example.batch_01.sample01.config;

import com.example.batch_01.sample01.etl.UserJdbcBatchItemWirter;
import com.example.batch_01.sample01.listener.JobCompletionNotificationListener;
import com.example.batch_01.sample01.model.User;
import com.example.batch_01.sample01.etl.UserFlatFileItemReader;
import com.example.batch_01.sample01.etl.UserItemProcessor;
import com.example.batch_01.sample01.tasklet.RecordCheckTasklet;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SampleBatchConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SampleBatchConfiguration.class);

    private final JobCompletionNotificationListener jobCompletionNotificationListener;
    private final UserFlatFileItemReader userFlatFileItemReader;
    private final UserItemProcessor userItemProcessor;
    private final UserJdbcBatchItemWirter userJdbcBatchItemWirter;
    private final RecordCheckTasklet recordCheckTasklet;

    private final DataSource dataSource;

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep, Step taskletStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .preventRestart()
                .flow(taskletStep)
                .next(sampleStep)
                .end()
                .build();
    }

    @Bean
    public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("sampleStep", jobRepository)
                .<User, User> chunk(50, transactionManager) // 10 Records at a time, max write
                .reader(userFlatFileItemReader.read())
                .processor(userItemProcessor)
                //.writer(writer())
                .writer(userJdbcBatchItemWirter)
                //.faultTolerant()
                //.skipLimit(10)
                //.skip(ArrayIndexOutOfBoundsException.class)
                //.retryLimit(3)
                //.retry(NullPointerException.class)
                .build();
    }
    @Bean
    public Step taskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("taskletStep", jobRepository)
                .tasklet(recordCheckTasklet, transactionManager)
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<User> writer() {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO user (id,first_name,last_name,email,gender,ip_address,country_code) VALUES (:id,:first_name,:last_name,:email,:gender,:ip_address,:country_code)")
                .dataSource(dataSource)
                .build();
    }

}
