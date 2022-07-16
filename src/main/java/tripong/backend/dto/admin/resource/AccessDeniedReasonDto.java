package tripong.backend.dto.admin.resource;

import lombok.Data;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AccessDeniedReasonDto {

    private String resourceName;
    private int priorityNum;
    private List<ResourceRolesDto> roleResources = new ArrayList<>();

    public AccessDeniedReasonDto(Resource resource){
        this.resourceName = resource.getResourceName();
        this.priorityNum = resource.getPriorityNum();
        this.roleResources = resource.getRoleResources().stream()
                .map(r -> new ResourceRolesDto(r)).collect(Collectors.toList());
    }
}
