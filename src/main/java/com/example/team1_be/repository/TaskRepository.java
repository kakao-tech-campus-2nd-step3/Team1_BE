package com.example.team1_be.repository;

import com.example.team1_be.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    void deleteById(Long projectId, Long taskId);
}
