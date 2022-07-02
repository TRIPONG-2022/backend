package tripong.backend.repository.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.user.User;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {




}
