package tripong.backend.entity.role;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleResource> roleResources = new ArrayList<>();


    ////-----편의 메소드-----
    public Role(String roleName, String description){
        this.roleName = roleName;
        this.description = description;
    }
}
