package com.thoughtworks.todoapp.repositories;

import com.thoughtworks.todoapp.models.ERole;
import com.thoughtworks.todoapp.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
