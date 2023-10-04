package com.imvux21.batchprocessingdemo.repository;

import com.imvux21.batchprocessingdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findAllByCountry(String country);
}
