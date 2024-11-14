package team1.be.seamless.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team1.be.seamless.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndIsDeleteFalse(String email);

    // Optional<UserEntity> findByEmailAndIsDeleteFalse(String email, Integer isDelete);
}
