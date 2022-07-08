package tripong.backend.entity.reply;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tripong.backend.entity.base.BaseEntity;
import tripong.backend.entity.user.User;
import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", nullable = false)
    private User userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ColumnDefault("0")
    private Long parentReply;
}
