package com.example.batch_01.sample11;

import com.example.batch_01.sample00.model.User;
import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessorListener implements ItemProcessListener<User, User> {
    private int count;

    @Override
    public void beforeProcess(User item) {
        count++;
        System.out.println("before processor : "+ count);
    }

    @Override
    public void afterProcess(User item, User result) {
        System.out.println("after processor : "+ count);

    }

    @Override
    public void onProcessError(User item, Exception e) {
        System.out.println("error processor : "+ count);

    }
}