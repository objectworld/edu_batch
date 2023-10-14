package com.example.batch_01.sample11;

public class CustomItemWriterListener implements ItemWriteListener<Customer2> {
    private int count;


    @Override
    public void beforeWrite(List<? extends Customer2> items) {
        count++;
        System.out.println("before writer : "+ count);
    }

    @Override
    public void afterWrite(List<? extends Customer2> items) {
        System.out.println("after writer : "+ count);

    }

    @Override
    public void onWriteError(Exception exception, List<? extends Customer2> items) {
        System.out.println("error writer : "+ count);

    }
}