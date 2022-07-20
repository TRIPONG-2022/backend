package tripong.backend.entity.tag;

import lombok.*;
import tripong.backend.entity.base.BaseTimeEntity;
import tripong.backend.entity.user.User;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String tagName;
}
