package tripong.backend.dto.admin.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.report.PostReport;
import tripong.backend.entity.report.ReportType;

import java.time.LocalDateTime;

@Data
public class GetPostReportedPageANDSearchResponseDto {

    //  게시글 pk, 신고 이유, 게시글 제목, 게시글 작성 시간, 작성자 pk, 작성자 아이디,
    //  작성자 닉네임, 신고자 아이디, 신고 시간
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

    public GetPostReportedPageANDSearchResponseDto(){}

    @QueryProjection
    public GetPostReportedPageANDSearchResponseDto(Long postId, ReportType reportType, String title, LocalDateTime postCreatedDate,
                                                   Long userId, String loginId, String nickName, String reporterLoginId, LocalDateTime reportCreatedDate){
        this.postId = postId;
        this.reportType = reportType;
        this.title = title;
        this.postCreatedDate = postCreatedDate;
        this.userId = userId;
        this.loginId = loginId;
        this.nickName = nickName;

        this.reporterLoginId = reporterLoginId;
        this.reportCreatedDate = reportCreatedDate;
    }

}

