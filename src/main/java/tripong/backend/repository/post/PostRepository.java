package tripong.backend.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripong.backend.entity.post.Category;
import tripong.backend.entity.post.Post;

import java.time.LocalDate;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategory(Category postType, Pageable pageable);

    @Query(value = "select * from post p where p.user_id = :userId and p.category = :postType and p.created_date between :fromDate and :endDate", nativeQuery = true)
    Page<Post> findByIdAndCategoryAndCreatedDate (@Param(value = "userId") Long userId, @Param(value = "postType") String postType, @Param(value = "fromDate") LocalDate fromDate, @Param(value = "endDate") LocalDate endDate, Pageable pageable);

    @Query(value = "select * from post p right outer join post_like l on p.id = l.post_id where l.user_id = :userId and p.category = :postType", nativeQuery = true)
    Page<Post> findLikePostByIdAndCategory (@Param(value = "userId") Long userId, @Param(value = "postType") String postType, Pageable pageable);

}