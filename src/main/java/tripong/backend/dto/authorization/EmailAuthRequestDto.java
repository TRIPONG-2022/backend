package tripong.backend.dto.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailAuthRequestDto {

    private String userId;
    private String email;
}
