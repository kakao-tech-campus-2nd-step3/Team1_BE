package team1.BE.seamless.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByIdAndIsDeletedFalse(Long id);

    Page<TaskEntity> findAllByProjectIdAndIsDeletedFalse(Long projectId, Pageable pageable);

    List<TaskEntity> findByProjectEntity(ProjectEntity projectEntity);

     Optional<TaskEntity> findByIdAndProjectEntityUserEntityEmail(Long id, String email);
}
