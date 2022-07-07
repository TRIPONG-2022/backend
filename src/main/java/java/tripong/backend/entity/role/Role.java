package java.tripong.backend.entity.role;

import lombok.*;
import tripong.backend.entity.role.RoleResource;
import tripong.backend.entity.role.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

    @Id @GeneratedValue
    private Long id;

    private String roleName;

    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleResource> roleResources = new ArrayList<>();


    //
    @Builder
    public Role(String roleName, String description){
        this.roleName = roleName;
        this.description = description;
    }
}
