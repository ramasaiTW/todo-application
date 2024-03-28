package com.thoughtworks.todoapp.repositories;

import com.thoughtworks.todoapp.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, Integer> {

    boolean existsByUser_Email(String email);

    Token findByUser_Email(String email);

    Token findByToken(String jwt);
}
