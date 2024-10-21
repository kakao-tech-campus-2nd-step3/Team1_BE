package team1.BE.seamless.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.BE.seamless.entity.OptionEntity;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {

    List<OptionEntity> findByIdIn(List<Long> ids);

    Page<OptionEntity> findAllByIsDeletedFalse(Pageable pageable);

    Optional<OptionEntity> findByIdAndIsDeletedFalse(Long id);

}
