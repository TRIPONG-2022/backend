package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EmailAuthRequestDto {

    @NotBlank
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;

}
