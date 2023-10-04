package com.imvux21.batchprocessingdemo.processor;

import com.imvux21.batchprocessingdemo.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ImportCustomerProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger log = LoggerFactory.getLogger(ImportCustomerProcessor.class);

    @Override
    public Customer process(Customer customer) throws Exception {
        return customer;
    }
}
