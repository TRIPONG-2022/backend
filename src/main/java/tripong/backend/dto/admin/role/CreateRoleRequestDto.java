package tripong.backend.dto.admin.role;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateRoleRequestDto {

    private String roleName; //서버단에서 ROLE_로 시작하는지 판단
    private String description;
}
