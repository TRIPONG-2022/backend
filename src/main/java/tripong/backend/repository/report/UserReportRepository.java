package tripong.backend.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import tripong.backend.entity.report.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

}
