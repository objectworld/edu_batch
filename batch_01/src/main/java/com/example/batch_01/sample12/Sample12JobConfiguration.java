package com.example.batch_01.sample12;

import com.example.batch_01.sample00.etl.UserItemProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Sample12JobConfiguration {
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private int chunkSize = 10;
    private final UserItemProcessor userItemProcessor;
    private final CustomItemReader customItemReader;
    @Bean
    public Job sample12() {
        return new JobBuilder("sample12", jobRepository)
                .start(sample12_step01())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @SneakyThrows
    @Bean
    public Step sample12_step01() {
        return new StepBuilder("sample12_step01",jobRepository)
                .<String, String>chunk(chunkSize,transactionManager)
                .reader(customItemReader)
                .processor(new ItemProcessor<String, String>() {

                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
                        // 3초 동안 item에 대해 processor 작업을 반복하는 방식
                        //repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));
                        repeatTemplate.iterate(new RepeatCallback() {
                            @Override
                            public RepeatStatus doInIteration(RepeatContext context) throws Exception {
                                System.out.println(item + " repeat");
                                return RepeatStatus.CONTINUABLE;
                            }
                        });
                        return item;
                    }
                })
                .writer(items -> System.out.println("items = " + items))
                .build();
    }
}