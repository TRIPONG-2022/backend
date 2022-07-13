package tripong.backend.dto.admin.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.report.UserReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReportedListResponseDto {

    //신고받은 유저 pk, 신고범주, 신고받은 유저 이름, 신고받은 유저 아이디, 신고받은 유저 닉네임,
    //신고받은 유저 권한들, 신고한 유저 아이디, 신고 시간
    private Long id;
    private ReportType reportType;
    private String name;
    private String loginId;
    private String nickName;
    private List<UserRolesDto> roles;
    private String reporterName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;


    public GetUserReportedListResponseDto(UserReport userReport){
        this.id = userReport.getReportedUserId().getId();
        this.reportType = userReport.getKind();
        this.name = userReport.getReportedUserId().getName();
        this.loginId = userReport.getReportedUserId().getLoginId();
        this.nickName = userReport.getReportedUserId().getNickName();
        this.roles = userReport.getReportedUserId().getUserRoles().stream()
                .map(u -> new UserRolesDto(u)).collect(Collectors.toList());
        this.reporterName = userReport.getReportUserId().getLoginId();
        this.createdDate = userReport.getCreatedDate();
    }



}
