package com.example.Ecom.Services;

import com.example.Ecom.Repositories.CustomerRepository;
import com.example.Ecom.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        try {
            return customerRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace(); // Add logging here
            throw e;
        }
    }


    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id){
        customerRepository.deleteById(id);
    }
}
