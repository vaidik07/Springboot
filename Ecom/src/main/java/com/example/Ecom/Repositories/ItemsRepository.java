package com.example.Ecom.Repositories;

import com.example.Ecom.model.Items;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemsRepository extends MongoRepository<Items , String> {
}
