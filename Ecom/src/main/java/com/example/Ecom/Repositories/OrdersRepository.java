package com.example.Ecom.Repositories;

import com.example.Ecom.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrdersRepository extends MongoRepository<Orders , String> {
}
