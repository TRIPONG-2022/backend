package tripong.backend.entity.report;


import lombok.*;
import tripong.backend.entity.base.BaseTimeEntity;
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
    @JoinColumn(referencedColumnName = "id", name = "report_id")
    private User reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "reported_id")
    private User reportedId;


    @Enumerated(EnumType.STRING)
    private ReportType kind;

    //
    @Builder
    public UserReport(User reportedId, User reportId, ReportType kind){
        this.reportedId = reportedId;
        this.reportId = reportId;
        this.kind=kind;
    }


}
