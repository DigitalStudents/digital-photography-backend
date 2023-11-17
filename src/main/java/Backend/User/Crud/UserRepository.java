package Backend.User.Crud;
import Backend.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByVerificationToken(String verificationToken);

//    @Query("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
//    void linkUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

}
