package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Log
    Optional<List<Project>> findAllByUserId(long userId);

    @Log
    Optional<Project> findByIdAndUser_Id(long projectId, long userId);

    @Log
    boolean existsByTitle(String title);

    @Log
    Optional<Project> findByTitle(String title);
}
