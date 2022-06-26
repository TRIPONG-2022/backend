package tripong.backend.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.report.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
}
