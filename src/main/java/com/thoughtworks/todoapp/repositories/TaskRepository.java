package com.thoughtworks.todoapp.repositories;

import com.thoughtworks.todoapp.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, Integer> {
    List<Task> findAllByUserId(long userId);
    Task findByIdAndUser_Id(long taskId, long userId);
}
