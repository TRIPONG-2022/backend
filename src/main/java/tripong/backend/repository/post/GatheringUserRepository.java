package tripong.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripong.backend.entity.post.GatheringUser;

public interface GatheringUserRepository extends JpaRepository<GatheringUser, Long> {

    @Query(value = "select * from gathering_user l where l.post_id = :postId and l.user_id = :userId", nativeQuery = true)
    GatheringUser findByPostIdAndUserId(@Param(value = "postId") Long postId, @Param(value = "userId") Long userId);
}
