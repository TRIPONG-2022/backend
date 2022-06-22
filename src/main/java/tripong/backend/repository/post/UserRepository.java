package tripong.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.post.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
