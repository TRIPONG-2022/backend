package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@DynamicUpdate
@NoArgsConstructor
public class PasswordRequestDto {

    private String userId;

    private String validLink;

    private String newPassword;

}
