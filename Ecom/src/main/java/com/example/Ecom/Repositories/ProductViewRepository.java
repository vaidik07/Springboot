package com.example.Ecom.Repositories;

import com.example.Ecom.model.ProductView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductViewRepository extends MongoRepository<ProductView, String> {
    Optional<ProductView> findByCustomerIdAndItemId(String customerId , String itemId);
}
