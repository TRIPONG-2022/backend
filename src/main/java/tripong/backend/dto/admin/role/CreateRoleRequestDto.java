package tripong.backend.dto.admin.role;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class CreateRoleRequestDto {

    @NotBlank(message = "입력이 필요합니다.")
    @Pattern(regexp = "^(ROLE_).*$", message = "ROLE_~~ 으로 작성해 주세요.")
    private String roleName;
    private String description;
}
