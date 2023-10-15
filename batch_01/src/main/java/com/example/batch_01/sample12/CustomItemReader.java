package com.example.batch_01.sample12;


import com.example.batch_01.sample13.CustomException;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
public class CustomItemReader implements ItemReader<String> {
    int i = 0;
    @Override
    public String read() throws CustomException {
        i++;
        if (i==3){
            throw new CustomException("skip exception");
        }
        System.out.println("itemReader : " + i);
        return i > 20 ? null : String.valueOf(i);
    }
}
