package team1.BE.seamless.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.BE.seamless.entity.MemberEntity;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Page<MemberEntity> findAllByProjectEntityIdAndIsDeleteFalse(Long projectId, Pageable pageable);

    Optional<MemberEntity> findByProjectEntityIdAndEmailAndIsDeleteFalse(Long projectId, String email);

    Optional<MemberEntity> findByProjectEntityIdAndIdAndIsDeleteFalse(Long projectId, Long memberId);

}
