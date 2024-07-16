package com.example.Ecom.Repositories;
import com.example.Ecom.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepository extends MongoRepository<Customer , String> {
}
