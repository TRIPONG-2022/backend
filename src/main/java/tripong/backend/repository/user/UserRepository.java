package tripong.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

}
