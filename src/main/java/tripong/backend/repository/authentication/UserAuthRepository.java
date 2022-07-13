package tripong.backend.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.user.User;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u set u.authentication = u.authentication + 1 WHERE u.loginId = :userId")
    void updateAuthenticationStatus(@Param("userId") String userId);

    @Query("SELECT u.loginId FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

}
