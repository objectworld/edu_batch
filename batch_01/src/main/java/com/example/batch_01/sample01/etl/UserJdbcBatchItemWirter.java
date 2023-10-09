package com.example.batch_01.sample01.etl;

import com.example.batch_01.sample01.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class UserJdbcBatchItemWirter implements ItemWriter<User> {

    private final DataSource dataSource;

    @Override

    public void write(Chunk<? extends User> data) throws Exception {
        System.out.println("로그출력");
        //TODO data.getItems() is not null 일경우 처리. ??? 안하면 wirter 안됨.
        JdbcBatchItemWriter<User> build = new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO user (id,first_name,last_name,email,gender,ip_address,country_code) VALUES (:id,:first_name,:last_name,:email,:gender,:ip_address,:country_code)")
                .dataSource(dataSource)
                .build();

    }
}