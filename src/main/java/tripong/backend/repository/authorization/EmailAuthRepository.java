package tripong.backend.repository.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.authorization.EmailValidLink;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailValidLink, String> {

    Optional<EmailValidLink> findByIdAndExpirationDateAfterAndExpired(String emailValidLink, LocalDateTime now, boolean isExpired);
}
