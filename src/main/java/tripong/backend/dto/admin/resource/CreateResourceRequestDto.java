package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.ResourceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResourceRequestDto {

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String description;

    private int priorityNum;

    private List<String> roleNames;

}
