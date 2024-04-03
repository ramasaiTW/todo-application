package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    Optional<User> findByEmail(String username);
    Boolean existsByEmail(String email);
}