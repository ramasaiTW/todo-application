package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Log
    Optional<User> findByEmail(String username);

    @Log
    Boolean existsByEmail(String email);
}
