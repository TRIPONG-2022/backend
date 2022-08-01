package tripong.backend.repository.mate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.user.User;


@Repository
public interface MateRepository extends JpaRepository<User, String> {

}
