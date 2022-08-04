package tripong.backend.entity.favor;

import lombok.*;
import tripong.backend.entity.base.BaseTimeEntity;
import tripong.backend.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "login_id")
    private User userId;

    @ElementCollection
    @Builder.Default
    private List<Integer> answer = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelerType travelerType;

}
