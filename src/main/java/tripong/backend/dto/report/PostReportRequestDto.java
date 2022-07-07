package tripong.backend.dto.report;


import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.report.PostReport;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.user.User;

@Data
@NoArgsConstructor
public class PostReportRequestDto {

    private ReportType kind;
    private User reportUserId;

    private Post reportedPostId;


    public PostReport toEntity(){
        return PostReport.builder()
                .reportedPostId(reportedPostId)
                .reportUserId(reportUserId)
                .kind(kind)
                .build();
    }

}
