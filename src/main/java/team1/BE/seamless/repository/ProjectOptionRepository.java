package team1.BE.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.BE.seamless.entity.ProjectEntity;
import team1.BE.seamless.entity.ProjectOption;

@Repository
public interface ProjectOptionRepository extends JpaRepository<ProjectOption, Long> {
    void deleteByProjectEntity(ProjectEntity projectEntity);

}
