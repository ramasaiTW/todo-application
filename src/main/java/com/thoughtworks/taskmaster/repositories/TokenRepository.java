package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Log
    boolean existsByUser_Email(String email);
    @Log
    Token findByUser_Email(String email);

    @Log
    Token findByToken(String jwt);
}
