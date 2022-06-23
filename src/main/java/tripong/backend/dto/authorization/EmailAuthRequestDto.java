package tripong.backend.dto.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class EmailAuthRequestDto {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String email;
}
