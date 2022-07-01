package tripong.backend.entity.reply;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tripong.backend.entity.base.BaseEntity;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reply_id")
    private BigInteger id;

    @Column(nullable = false)
    private BigInteger postId;

    @Column(nullable = false)
    private BigInteger userId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name ="parent_reply_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reply parentReply;

    @OneToMany (mappedBy = "parentReply", cascade = CascadeType.ALL)
    private List<Reply> childrenReply = new ArrayList<>();

}
