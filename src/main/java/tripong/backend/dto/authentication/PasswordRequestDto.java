package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class PasswordRequestDto {

    private String userId;

    private String validLink;

    @NotEmpty
    private String newPassword;
}
