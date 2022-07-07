package java.tripong.backend.entity.report;


import lombok.*;
import tripong.backend.entity.base.BaseTimeEntity;
import tripong.backend.entity.post.Post;
import tripong.backend.entity.report.ReportType;
import tripong.backend.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReport extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "report_user_id")
    private User reportUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "reported_post_id")
    private Post reportedPostId;


    @Enumerated(EnumType.STRING)
    private tripong.backend.entity.report.ReportType kind;

    //
    @Builder
    public PostReport(Post reportedPostId, User reportUserId, ReportType kind){
        this.reportedPostId = reportedPostId;
        this.reportUserId = reportUserId;
        this.kind=kind;
    }

}
