package tripong.backend.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
