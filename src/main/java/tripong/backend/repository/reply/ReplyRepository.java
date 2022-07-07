package tripong.backend.repository.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.entity.reply.Reply;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findById(Long id);

    @Query("SELECT r FROM Reply r WHERE r.postId = :postId ORDER BY r.createdDate ASC")
    Page<Reply> getReplyListByUserId(@Param("postId") String postId, Pageable pageable);

    @Query("SELECT r FROM Reply r WHERE r.postId = :postId AND r.parentReply = 0 ORDER BY r.createdDate ASC")
    Page<Reply> findParentReplyByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT r FROM Reply r WHERE r.postId = :postId AND r.parentReply = :parentReply ORDER BY r.createdDate ASC")
    List<Reply> findChildrenReplyByPostId(@Param("postId") Long postId, @Param("parentReply") Long parentReply, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Reply r WHERE r.id = :#{#reply.id}")
    void deleteById(@Param("reply") Reply reply);

}
