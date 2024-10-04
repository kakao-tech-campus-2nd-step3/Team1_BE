package team1.BE.seamless.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.BE.seamless.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndIsDeleteFalse(String email);

    Optional<UserEntity> findByEmailAndIsDeleteFalse(String email, Integer isDelete);
}
