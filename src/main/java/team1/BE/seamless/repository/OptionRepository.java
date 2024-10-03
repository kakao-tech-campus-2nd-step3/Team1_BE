package team1.BE.seamless.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.BE.seamless.entity.OptionEntity;
import team1.BE.seamless.entity.ProjectEntity;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

    List<OptionEntity> findByIdIn(List<Long> ids);

}
