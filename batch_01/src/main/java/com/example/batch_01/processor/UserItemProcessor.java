package com.example.batch_01.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.batch_01.config.BatchConfiguration;
import com.example.batch_01.model.User;

public class UserItemProcessor implements ItemProcessor<User, User> {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    @Override
    public User process(User user) throws Exception {
        logger.info("item processed");
        if(user.getCountry_code().equals("US"))
        {
        	logger.info("{},미국인입니다.",user.toString());
            return user;
        } else {
            return null;
        }
    }
}
