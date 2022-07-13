package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import tripong.backend.entity.role.ResourceType;

@Data
@AllArgsConstructor
public class DeleteResourceReloadDto {
    private ResourceType resourceType;
    private String resourceName;
}
