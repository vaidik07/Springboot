package com.springrest.springrestmongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.springrest.springrestmongo.model.TodoDTO;

public interface TodoRepository extends MongoRepository<TodoDTO, String> {
}
