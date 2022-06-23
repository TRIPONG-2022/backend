package tripong.backend.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategory(Category postType, Pageable pageable);
}
