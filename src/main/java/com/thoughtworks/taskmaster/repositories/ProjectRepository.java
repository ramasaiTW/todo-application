package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, Integer> {
    Optional<List<Project>> findAllByUserId(long userId);
    Optional<Project> findByIdAndUser_Id(long projectId, long userId);
}