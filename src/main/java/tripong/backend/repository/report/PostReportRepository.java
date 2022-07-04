package tripong.backend.repository.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.report.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    @EntityGraph(attributePaths = {"reportedPostId", "reportUserId"})
    @Query(value = "select pr from PostReport pr")
    Page<PostReport> findReportPostANDReportedPostANDReportUserPagingAll(Pageable pageable);
}
