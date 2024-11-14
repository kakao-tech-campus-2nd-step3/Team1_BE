package team1.be.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.be.seamless.entity.ProjectEntity;
import team1.be.seamless.entity.ProjectOptionEntity;

@Repository
public interface ProjectOptionRepository extends JpaRepository<ProjectOptionEntity, Long> {

    void deleteByProjectEntity(ProjectEntity projectEntity);

}
