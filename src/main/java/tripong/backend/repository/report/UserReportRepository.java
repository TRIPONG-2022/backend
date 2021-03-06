package tripong.backend.repository.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.entity.report.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {


    @EntityGraph(attributePaths = {"reportedUserId", "reportUserId"})
    @Query(value = "select ur from UserReport ur")
    Page<UserReport> findReportUserANDReportedUserPagingAll(Pageable pageable);

}
