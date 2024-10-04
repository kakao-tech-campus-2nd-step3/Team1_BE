package team1.BE.seamless.repository;

import java.util.List;
import java.util.Optional;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByProjectEntity(ProjectEntity projectEntity);

    Optional<TaskEntity> findByIdAndProjectEntityUserEmail(Long id, String email);
}
