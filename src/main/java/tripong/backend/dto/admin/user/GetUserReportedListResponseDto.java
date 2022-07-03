package tripong.backend.dto.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.dto.admin.resource.ResourceRolesDto;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.report.UserReport;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.entity.role.UserRole;
import tripong.backend.entity.user.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReportedListResponseDto {

    //유저 pk, 유저이름, 유저 닉네임, 유저 아이디, 권한들 반환필요
    private Long id;
    private ReportType reportType;
    private String name;
    private String loginId;
    private String nickName;
    private List<UserRolesDto> roles;
    private String reporterName;


    public GetUserReportedListResponseDto(UserReport userReport){
        this.id = userReport.getReportedUserId().getId();
        this.reportType = userReport.getKind();
        this.name = userReport.getReportedUserId().getName();
        this.loginId = userReport.getReportedUserId().getLoginId();
        this.nickName = userReport.getReportedUserId().getNickName();
        this.roles = userReport.getReportedUserId().getUserRoles().stream()
                .map(u -> new UserRolesDto(u)).collect(Collectors.toList());
        this.reporterName = userReport.getReportUserId().getLoginId();

    }



}
