package tripong.backend.entity.role;

import lombok.*;
import tripong.backend.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRole {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    //
    public void injectUser(User user) {this.user=user;}


    @Builder
    public UserRole(User user, Role role){
        this.user=user;
        this.role= role;
    }
}
