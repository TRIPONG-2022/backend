package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
public class PasswordRequestDto {

    @NotEmpty
    private String validLink;

    @NotBlank(groups = AuthValidationGroup.groupA.class)
    @Length(min=4, max=15, message = "4자 이상, 15자 이하로 설정해 주세요.")
    private String newPassword;

}
