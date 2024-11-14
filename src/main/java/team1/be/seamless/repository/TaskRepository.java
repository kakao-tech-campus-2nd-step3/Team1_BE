package team1.be.seamless.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByIdAndIsDeletedFalse(Long id);

    List<TaskEntity> findByOwnerIdAndProjectEntityAndIsDeletedFalse(Long memberId,
        ProjectEntity project);

    Page<TaskEntity> findAllByProjectEntityIdAndIsDeletedFalse(Long projectId, Pageable pageable);

    Optional<TaskEntity> findByIdAndProjectEntityUserEntityEmail(Long id, String email);

    @Query(value = "SELECT * FROM taskss t WHERE t.project_id = :projectId " +
        "AND (t.is_deleted = false) " +
        "AND (:status IS NULL OR t.status = :status) " +
        "AND (:priority IS NULL OR t.priority = :priority) " +
        "AND (:memberId IS NULL OR t.member_id = :memberId)",
        countQuery = "SELECT count(*) FROM taskss t WHERE t.project_id = :projectId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:memberId IS NULL OR t.member_id = :memberId)",
        nativeQuery = true)
    Page<TaskEntity> findByProjectIdAndOptionalFilters(
        @Param("projectId") Long projectId,
        @Param("status") String status,
        @Param("priority") String priority,
        @Param("memberId") Long memberId,
        Pageable pageable
    );
}
