package tripong.backend.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.user.User;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<User, String> {

    Optional<User> findByLoginId(String loginId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u set u.authentication = u.authentication + 1 WHERE u.loginId = :userId")
    void updateauthenticationStatus(String userId);

    Optional<User> findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u set u.password = :newPassword WHERE u.loginId = :userId")
    int changePassword(String newPassword, String userId);

}
