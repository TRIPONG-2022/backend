package tripong.backend.dto.admin.role;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class DeleteRoleRequestDto {

    @NotBlank
    private String roleName;

}
