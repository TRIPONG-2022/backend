package tripong.backend.dto.admin.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.dto.admin.user.UserRolesDto;
import tripong.backend.entity.report.PostReport;
import tripong.backend.entity.report.ReportType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPostReportedListResponseDto {

//  게시글 pk, 신고 이유, 게시글 제목, 게시글 작성 시간, 작성자 pk, 작성자 아이디, 작성자 닉네임, 신고자 아이디, 신고 시간
    private Long postId;
    private ReportType reportType;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime postCreatedDate;
    private Long userId;
    private String loginId;
    private String nickName;

    private String reporterLoginId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime reportCreatedDate;

    public GetPostReportedListResponseDto(PostReport postReport){
        this.postId = postReport.getReportedPostId().getId();
        this.reportType = postReport.getKind();
        this.title = postReport.getReportedPostId().getTitle();
        this.postCreatedDate = postReport.getReportedPostId().getCreatedDate();
        this.userId = postReport.getReportUserId().getId();
        this.loginId = postReport.getReportedPostId().getUser().getLoginId();
        this.nickName = postReport.getReportedPostId().getUser().getNickName();

        this.reporterLoginId = postReport.getReportUserId().getLoginId();
        this.reportCreatedDate = postReport.getCreatedDate();
    }

}

