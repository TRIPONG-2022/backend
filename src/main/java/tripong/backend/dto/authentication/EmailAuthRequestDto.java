package tripong.backend.dto.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailAuthRequestDto {

    private String userId;

    private String email;

}
