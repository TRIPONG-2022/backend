package tripong.backend.entity.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    private List<RoleResource> roleResources = new ArrayList<>();

}
