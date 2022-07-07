package java.tripong.backend.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class NormalLoginRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

}
