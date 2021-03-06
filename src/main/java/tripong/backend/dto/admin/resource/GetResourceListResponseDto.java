package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.Resource;
import tripong.backend.entity.role.ResourceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetResourceListResponseDto {

    private Long resourceId;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String description;

    private int priorityNum;

    private List<ResourceRolesDto> roleResources = new ArrayList<>();

    ///
    public GetResourceListResponseDto(Resource resource){
        this.resourceId = resource.getId();
        this.resourceName = resource.getResourceName();
        this.resourceType = resource.getResourceType();
        this.description = resource.getDescription();
        this.priorityNum = resource.getPriorityNum();
        this.roleResources = resource.getRoleResources().stream()
                .map(r -> new ResourceRolesDto(r)).collect(Collectors.toList());
    }

}
