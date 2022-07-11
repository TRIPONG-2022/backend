package tripong.backend.repository.reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tripong.backend.dto.reply.ReplyResponseDto;
import tripong.backend.entity.reply.Reply;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findById(Long id);

    // @Query(value = "SELECT * FROM Reply r JOIN Reply p WHERE r.login_id = :userId AND r.created_date BETWEEN :fromDate AND :endDate", nativeQuery = true)
    @Query("SELECT new tripong.backend.dto.reply.ReplyResponseDto(r.createdDate, r.modifiedDate, r.id, r.postId, u.loginId, r.content, r.parentReply.id) FROM Reply r JOIN r.userId u WHERE u.loginId = :userId AND r.createdDate BETWEEN :fromDate AND :endDate")
    Page<ReplyResponseDto> findReplyByUserId(@Param("userId") String userId, @Param("fromDate") LocalDateTime fromDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT new tripong.backend.dto.reply.ReplyResponseDto(r.createdDate, r.modifiedDate, r.id, r.postId, u.loginId, r.content, r.parentReply.id) FROM Reply r JOIN r.userId u WHERE r.postId = :postId AND r.parentReply.id IS NULL")
    Page<ReplyResponseDto> findParentReplyByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT new tripong.backend.dto.reply.ReplyResponseDto(r.createdDate, r.modifiedDate, r.id, r.postId, u.loginId, r.content, r.parentReply.id) FROM Reply r JOIN r.userId u WHERE r.postId = :postId AND r.parentReply.id = :parentReply")
    Page<ReplyResponseDto> findChildrenReplyByPostId(@Param("postId") Long postId, @Param("parentReply") Long parentReply, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Reply r WHERE r.id = :#{#reply.id}")
    void deleteById(@Param("reply") Reply reply);

}
