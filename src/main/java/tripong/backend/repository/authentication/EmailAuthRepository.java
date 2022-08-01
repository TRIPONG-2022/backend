package tripong.backend.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.authentication.EmailValidLink;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailValidLink, String> {

    Optional<EmailValidLink> findByIdAndExpirationDateAfterAndExpired(String emailValidLink, LocalDateTime now, boolean isExpired);

    @Query(value = "SELECT * FROM email_valid_link e WHERE e.user_id = :userId ORDER BY e.created_time DESC LIMIT 1", nativeQuery = true)
    Optional<EmailValidLink> findByTheLatestEmailToken(@Param("userId") String userId);

}
