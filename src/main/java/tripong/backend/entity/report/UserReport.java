package tripong.backend.entity.report;


import lombok.*;
import tripong.backend.entity.base.BaseTimeEntity;
import tripong.backend.entity.user.JoinType;
import tripong.backend.entity.user.User;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReport extends BaseTimeEntity {


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "report_user_id")
    private User reportUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "reported_user_id")
    private User reportedUserId;


    @Enumerated(EnumType.STRING)
    private ReportType kind;


    ////-----편의 메소드-----
    public UserReport(User reportedUserId, User reportUserId, ReportType kind){
        this.reportedUserId = reportedUserId;
        this.reportUserId = reportUserId;
        this.kind=kind;
    }
}
