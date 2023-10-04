package com.imvux21.batchprocessingdemo.job;

import com.imvux21.batchprocessingdemo.entity.Customer;
import com.imvux21.batchprocessingdemo.listener.JobCompletionNotificationListener;
import com.imvux21.batchprocessingdemo.processor.ImportCustomerProcessor;
import com.imvux21.batchprocessingdemo.repository.CustomerRepository;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@RequiredArgsConstructor
public class ImportCustomerJobConfig {
    private final CustomerRepository customerRepository;

    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    private final JobCompletionNotificationListener listener;

    private final TaskExecutor taskExecutor;

    public FlatFileItemReader<Customer> reader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .resource(new ClassPathResource("customers.csv"))
                .linesToSkip(1) // skip header
                .delimited()// default is comma, can be changed( .delimiter(";"), .delimiter("|"), etc.)
                .names("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob") // names of the columns
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Customer.class);
                }}) // maps columns to properties of the Customer class
                .build();
    }

    public ImportCustomerProcessor processor() {
        return new ImportCustomerProcessor();
    }

    public RepositoryItemWriter<Customer> writer() {
        return new RepositoryItemWriterBuilder<Customer>()
                .repository(customerRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Job importCustomerJob () {

        return new JobBuilder("importCustomerJob",jobRepository)
                .preventRestart()// don't restart if job already exists
                .incrementer(new RunIdIncrementer())// increment job id on each run to save JobRepository
                .listener(listener)
                .flow(step1())// step1 is the only step in this job
                .end()// end the flow
                .build();
    }

    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                // read 10 records at a time,
                // commit after 10 records,
                // transaction manager to save the state of the job
                // and use the task executor to run the job in parallel
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor) // parallel processing
                .build();
    }
}
