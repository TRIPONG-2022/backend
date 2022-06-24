package tripong.backend.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.user.User;

@Data
@NoArgsConstructor
public class UserReportRequestDto {

    private ReportType kind;

    private User reportId;
    private User reportedId;


    public UserReport toEntity(){
        return UserReport.builder()
                .reportedId(reportedId)
                .reportId(reportId)
                .kind(kind)
                .build();
    }
}
