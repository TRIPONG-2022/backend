package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.ResourceType;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResourceFormRequestDto {

    private List<String> roles;
    private List<ResourceType> resourceTypes;

    public CreateResourceFormRequestDto(List<String> roles){
        this.roles=roles;
        this.resourceTypes = Arrays.asList(ResourceType.values());
    }


}
