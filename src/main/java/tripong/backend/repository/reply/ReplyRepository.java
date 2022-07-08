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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findById(Long id);

    // @Query(value = "SELECT new tripong.backend.dto.reply.ReplyResponseDto(r.createdDate, r.lastModifiedDate, r.id, r.postId, u.loginId, r.content, r.parentReply) FROM Reply r JOIN r.userId u WHERE r.userId.loginId = :userId")
    @Query(value = "SELECT * FROM Reply r WHERE r.login_id = :userId AND r.created_date BETWEEN :fromDate AND :endDate", nativeQuery = true)
    Page<Reply> getReplyListByUserId(@Param("userId") Long userId, @Param(value = "fromDate") LocalDate fromDate, @Param(value = "endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM Reply r WHERE r.postId = :postId AND r.parentReply = 0")
    Page<Reply> findParentReplyByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT r FROM Reply r WHERE r.postId = :postId AND r.parentReply = :parentReply")
    List<Reply> findChildrenReplyByPostId(@Param("postId") Long postId, @Param("parentReply") Long parentReply, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Reply r WHERE r.id = :#{#reply.id}")
    void deleteById(@Param("reply") Reply reply);

}
