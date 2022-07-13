package tripong.backend.dto.admin.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripong.backend.entity.role.ResourceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResourceRequestDto {

    @NotBlank(message = "자원명이 입력되지 않았습니다.")
    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String description;

    @Min(value = 1, message = "양수만 입력 가능합니다.")
    private Integer priorityNum;

    @NotBlank(message = "권한이 선택되지 않았습니다.")
    private List<String> roleNames;
}
