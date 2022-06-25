package tripong.backend.repository.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.user.User;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<User, String> {

    Optional<User> findByLoginId(String loginId);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.authentication = u.authentication + 1 WHERE u.loginId = :userId")
    void updateauthenticationStatus(String userId);

    Optional<User> findByEmail(String email);

}
