package com.example.batch_01.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.batch_01.config.BatchConfiguration;
import com.example.batch_01.model.Tatbestand;

public class TatbestandItemProcessor implements ItemProcessor<Tatbestand, Tatbestand> {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    @Override
    public Tatbestand process(Tatbestand tatbestand) throws Exception {
        logger.info("item processed");
        if(tatbestand.getBetrag() >= 100)
        {
            return tatbestand;
        } else {
            return null;
        }
    }
}
