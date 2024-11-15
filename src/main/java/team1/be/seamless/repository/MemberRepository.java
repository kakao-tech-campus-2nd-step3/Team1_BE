package team1.be.seamless.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.be.seamless.entity.MemberEntity;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Page<MemberEntity> findAllByProjectEntityIdAndIsDeleteFalse(Long projectId, Pageable pageable);

    Optional<MemberEntity> findByProjectEntityIdAndEmailAndIsDeleteFalse(Long projectId,
                                                                         String email);

    Optional<MemberEntity> findByProjectEntityIdAndIdAndIsDeleteFalse(Long projectId,
                                                                      Long memberId);

    Optional<MemberEntity> findByEmailAndIsDeleteFalse(String email);

    Optional<MemberEntity> findByName(String name);

    Optional<MemberEntity> findByIdAndIsDeleteFalse(Long id);

}
