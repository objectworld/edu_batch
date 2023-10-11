package com.imvux21.batchprocessingdemo.job;

import com.imvux21.batchprocessingdemo.entity.Customer;
import com.imvux21.batchprocessingdemo.listener.JobCompletionNotificationListener;
import com.imvux21.batchprocessingdemo.processor.ExportCustomerProcessor;
import com.imvux21.batchprocessingdemo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileNotFoundException;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ExportCustomerJobConfig {

    private final CustomerRepository customerRepository;

    private final JobRepository jobRepository;

    private final JobCompletionNotificationListener listener;

    private final TaskExecutor taskExecutor;

    private final PlatformTransactionManager transactionManager;

    public RepositoryItemReader<Customer> reader() {
        return new RepositoryItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .repository(customerRepository)
//                .arguments("Vietnam")
//                .methodName("findByCountry")
                .methodName("findAll")
                .pageSize(100)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    public ExportCustomerProcessor processor() {
        return new ExportCustomerProcessor();
    }

    public FlatFileItemWriter<Customer> writer() {
        return new FlatFileItemWriterBuilder<Customer>()
                .append(true)
                .name("customerItemWriter")
                .resource(new FileSystemResource("src/main/resources/vietnam-customers.csv"))
                .lineAggregator(customer -> customer.getId() + "," + customer.getFirstName() + "," + customer.getLastName() + "," + customer.getEmail() + "," + customer.getGender() + "," + customer.getContactNo() + "," + customer.getCountry() + "," + customer.getDob())
                .delimited()
                .names("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob")
                .headerCallback(writer -> writer.write("id,firstName,lastName,email,gender,contactNo,country,dob"))
                .build();
    }

    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
//                .faultTolerant() // enable fault tolerance to skip exceptions
//                .skipLimit(100) // skip up to 100 exceptions
//                .skip(Exception.class)// skip all exceptions
//                .noSkip(FileNotFoundException.class)// do not skip FileNotFoundException
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job exportCustomerJob() {
        return new JobBuilder("exportCustomerJob", jobRepository)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }
}
