package tripong.backend.dto.admin.resource;

import lombok.Data;
import tripong.backend.entity.role.RoleResource;

@Data
public class ResourceRolesDto {

    private String roleName;

    public ResourceRolesDto(RoleResource roleResource){
        this.roleName = roleResource.getRole().getRoleName();
    }
}