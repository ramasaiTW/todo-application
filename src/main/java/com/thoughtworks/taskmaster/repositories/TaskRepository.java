package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, Integer> {
    Optional<List<Task>> findAllByUserId(long userId);
    Optional<Task> findByIdAndUser_Id(long taskId, long userId);
}
