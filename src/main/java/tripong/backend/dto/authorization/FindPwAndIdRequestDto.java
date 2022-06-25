package tripong.backend.dto.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class FindPwAndIdRequestDto {

    @NotEmpty
    private String email;

}
