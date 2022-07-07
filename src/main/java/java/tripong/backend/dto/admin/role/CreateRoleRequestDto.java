package java.tripong.backend.dto.admin.role;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateRoleRequestDto {

    @NotBlank
    private String roleName;

    private String description;



}
