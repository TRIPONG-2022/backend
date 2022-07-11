package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordRequestDto {

    private String userId;

    private String validLink;

    private String newPassword;

}
