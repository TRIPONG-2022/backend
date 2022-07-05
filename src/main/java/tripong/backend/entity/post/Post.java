package tripong.backend.entity.post;


import lombok.Getter;
import lombok.Setter;
import tripong.backend.entity.base.BaseEntity;
import tripong.backend.entity.user.User;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String title;

    private Long tmp;
}
