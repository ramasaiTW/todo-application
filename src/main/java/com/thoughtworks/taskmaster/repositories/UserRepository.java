package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);

    Boolean existsByEmail(String email);
}
