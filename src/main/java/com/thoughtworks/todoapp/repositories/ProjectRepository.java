package com.thoughtworks.todoapp.repositories;

import com.thoughtworks.todoapp.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, Integer> {
    List<Project> findAllByUserId(long userId);
    Project findByIdAndUser_Id(long projectId, long userId);
}
