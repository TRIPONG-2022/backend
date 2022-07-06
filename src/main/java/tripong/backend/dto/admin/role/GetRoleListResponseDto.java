package tripong.backend.dto.admin.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRoleListResponseDto {

    private Long roleId;
    private String roleName;
    private String description;
}
