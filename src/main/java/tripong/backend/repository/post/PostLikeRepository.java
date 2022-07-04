package tripong.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripong.backend.entity.post.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query(value = "select * from post_like l where l.post_id = :postId and l.user_id = :userId", nativeQuery = true)
    PostLike findByPostIdAndUserId(@Param(value = "postId") Long postId, @Param(value = "userId") Long userId);
}
