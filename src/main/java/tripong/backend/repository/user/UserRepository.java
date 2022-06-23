package tripong.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByNickName(String nickName);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.authentication = u.authentication + 1 WHERE u.loginId = :userId")
    int updateauthenticationStatus(String userId);


}
