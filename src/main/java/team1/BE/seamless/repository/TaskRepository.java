package team1.BE.seamless.repository;

import team1.BE.seamless.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

//    void deleteById(Long projectId, Long taskId);
}
