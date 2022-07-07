package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UserAuthRequestDto {

    @NotEmpty
    private String email;

}
