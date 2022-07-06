package tripong.backend.dto.admin.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.ResourceType;
import tripong.backend.entity.role.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateResourceRequestDto {

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String methodName;

    private int priorityNum;

    private List<String> roleNames;

}