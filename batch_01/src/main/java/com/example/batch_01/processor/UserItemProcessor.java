package com.example.batch_01.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.batch_01.config.SampleBatchConfiguration;
import com.example.batch_01.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {
    private static final Logger logger = LoggerFactory.getLogger(SampleBatchConfiguration.class);
    @Override
    public User process(User user) throws Exception {
        logger.info("item processed");
        if(user.getCountry_code().equals("US"))
        {
        	logger.info("{}\n {} {} : 미국인입니다.",user.toString(),user.getLast_name(),user.getFirst_name());
            return user;
        } else {
            return null;
        }
    }
}
