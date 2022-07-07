package tripong.backend.dto.admin.user;

import lombok.Data;
import tripong.backend.entity.role.UserRole;

@Data
public class UserRolesDto {

    private String roleName;

    public UserRolesDto(UserRole userRole){
        this.roleName = userRole.getRole().getRoleName();
    }
}
