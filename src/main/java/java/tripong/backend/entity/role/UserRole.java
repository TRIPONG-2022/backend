package java.tripong.backend.entity.role;

import lombok.*;
import tripong.backend.entity.role.Role;
import tripong.backend.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRole {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private tripong.backend.entity.role.Role role;



    //
    public void injectUser(User user) {this.user=user;}


    @Builder
    public UserRole(User user, Role role){
        this.user=user;
        this.role= role;
    }
}
