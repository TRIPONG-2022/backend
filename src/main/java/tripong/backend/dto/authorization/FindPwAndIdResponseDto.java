package tripong.backend.dto.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindPwAndIdResponseDto {

    private String userId;
    private String password;

}
