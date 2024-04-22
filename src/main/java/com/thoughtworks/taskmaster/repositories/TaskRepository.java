package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Log
    Optional<List<Task>> findAllByUserId(long userId);
    @Log
    Optional<Task> findByIdAndUser_Id(long taskId, long userId);

    Optional<List<Task>> findAllByUserIdAndProjectIdOrderByDeadlineAscPriorityAsc(long userId, long projectId);
}
