package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.RoleResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRolesDto {

    private String roleName;

    public ResourceRolesDto(RoleResource roleResource){
        this.roleName = roleResource.getRole().getRoleName();
    }
}