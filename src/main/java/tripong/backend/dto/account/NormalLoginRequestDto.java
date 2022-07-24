package tripong.backend.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class NormalLoginRequestDto {

    @NotBlank(message = "입력이 필요합니다.")
    private String loginId;

    @NotBlank(message = "입력이 필요합니다.")
    private String password;
}
