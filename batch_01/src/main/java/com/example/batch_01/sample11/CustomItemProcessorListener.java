package com.example.batch_01.sample11;

public class CustomItemProcessorListener implements ItemProcessListener<Customer, Customer2> {
    private int count;

    @Override
    public void beforeProcess(Customer item) {
        count++;
        System.out.println("before processor : "+ count);
    }

    @Override
    public void afterProcess(Customer item, Customer2 result) {
        System.out.println("after processor : "+ count);

    }

    @Override
    public void onProcessError(Customer item, Exception e) {
        System.out.println("error processor : "+ count);

    }
}