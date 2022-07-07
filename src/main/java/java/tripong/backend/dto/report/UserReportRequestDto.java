package java.tripong.backend.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.user.User;

@Data
@NoArgsConstructor
public class UserReportRequestDto {

    private ReportType kind;
    private User reportUserId;

    private User reportedUserId;


    public UserReport toEntity(){
        return UserReport.builder()
                .reportedUserId(reportedUserId)
                .reportUserId(reportUserId)
                .kind(kind)
                .build();
    }
}
